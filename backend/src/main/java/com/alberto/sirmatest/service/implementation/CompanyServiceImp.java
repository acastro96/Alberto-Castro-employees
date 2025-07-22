package com.alberto.sirmatest.service.implementation;

import com.alberto.sirmatest.dto.CompanyInformation;
import com.alberto.sirmatest.dto.EmployeePair;
import com.alberto.sirmatest.dto.PairResult;
import com.alberto.sirmatest.service.contract.CompanyService;
import com.alberto.sirmatest.util.Utility;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CompanyServiceImp implements CompanyService {

  private final ExecutorService executor;
  private static final Logger log = LoggerFactory.getLogger(CompanyServiceImp.class);

  public CompanyServiceImp(@Qualifier("processingExecutor") ExecutorService executor) {
    this.executor = executor;
  }

  /**
   * Method that receive the project and the employees information and return the pair of employees that has worked
   * more time together
   *
   * @param companyInformationList the Dto that has the information
   * @return List of PairResult object with the ids of the employees that has worked more time together.
   */
  @Override
  public List<PairResult> findLongestPartnersByProject(List<CompanyInformation> companyInformationList) {
    Map<Integer, List<CompanyInformation>> projects = companyInformationList.stream()
        .collect(Collectors.groupingBy(CompanyInformation::projectId));

    Map<EmployeePair, Long> pairTotalDays = new ConcurrentHashMap<>();
    Map<EmployeePair, Set<Integer>> pairProjects = new ConcurrentHashMap<>();

    log.info("Creating tasks for processing the data");
    List<CompletableFuture<Void>> tasks = getProcessTasks(projects, pairTotalDays, pairProjects);
    log.info("Executing tasks");
    CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();

    OptionalLong maxDaysOpt = pairTotalDays.values().stream().mapToLong(Long::longValue).max();
    if (maxDaysOpt.isEmpty()) return List.of();

    long maxDays = maxDaysOpt.getAsLong();

    return getPairResultList(pairTotalDays, maxDays, pairProjects);
  }

  private List<CompletableFuture<Void>> getProcessTasks(Map<Integer, List<CompanyInformation>> projects, Map<EmployeePair, Long> pairTotalDays, Map<EmployeePair, Set<Integer>> pairProjects) {
    return projects.values().stream()
        .filter(p -> p.size() >= 2)
        .map(p -> CompletableFuture.runAsync(() -> {
          int n = p.size();
          IntStream.range(0, n - 1).forEach(i ->
              IntStream.range(i + 1, n).forEach(j -> {
                CompanyInformation a = p.get(i);
                CompanyInformation b = p.get(j);

                long days = Utility.getOverlapDays(
                    a.dateFrom(), a.dateTo(),
                    b.dateFrom(), b.dateTo());

                if (days > 0) {
                  EmployeePair pair = new EmployeePair(a.empId(), b.empId());
                  pairTotalDays.merge(pair, days, Long::sum);
                  pairProjects.computeIfAbsent(pair, k -> ConcurrentHashMap.newKeySet()).add(a.projectId());
                }
              })
          );
        }, this.executor))
        .toList();
  }

  private List<PairResult> getPairResultList(Map<EmployeePair, Long> pairTotalDays, long maxDays, Map<EmployeePair, Set<Integer>> pairProjects) {
    return pairTotalDays.entrySet().stream()
        .filter(n -> n.getValue() == maxDays)
        .map(entry -> new PairResult(
            entry.getKey().emp1(),
            entry.getKey().emp2(),
            entry.getValue(),
            new ArrayList<>(pairProjects.getOrDefault(entry.getKey(), Set.of()))))
        .toList();
  }
}
