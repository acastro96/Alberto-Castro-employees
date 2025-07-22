package com.alberto.sirmatest.service;

import com.alberto.sirmatest.dto.CompanyInformation;
import com.alberto.sirmatest.dto.PairResult;
import com.alberto.sirmatest.service.implementation.CompanyServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

  private final CompanyServiceImp service = new CompanyServiceImp(Executors.newFixedThreadPool(2));

  @Test
  void shouldReturnPairWithOverlap() {
    List<CompanyInformation> input = List.of(
        new CompanyInformation(1, 100, "2022-01-01", "2022-03-01"),
        new CompanyInformation(2, 100, "2022-02-01", "2022-04-01")
    );

    List<PairResult> result = service.findLongestPartnersByProject(input);

    assertThat(result).isNotEmpty();
    assertThat(result.getFirst().emp1Id()).isEqualTo(1);
    assertThat(result.getFirst().emp2Id()).isEqualTo(2);
    assertThat(result.getFirst().totalDays()).isEqualTo(29);
  }

  @Test
  void shouldReturnEmptyWhenNoOverlap() {
    List<CompanyInformation> input = List.of(
        new CompanyInformation(1, 100, "2022-01-01", "2022-02-01"),
        new CompanyInformation(2, 100, "2022-03-01", "2022-04-01")
    );

    List<PairResult> result = service.findLongestPartnersByProject(input);

    assertThat(result).isEmpty();
  }

}
