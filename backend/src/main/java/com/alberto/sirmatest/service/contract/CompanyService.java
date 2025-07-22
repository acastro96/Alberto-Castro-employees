package com.alberto.sirmatest.service.contract;

import com.alberto.sirmatest.dto.CompanyInformation;
import com.alberto.sirmatest.dto.PairResult;

import java.util.List;

public interface CompanyService {

  /**
   * Method that receive the project and the employees information and return the pair of employees that has worked
   * more time together
   * @param companyInformationList the Dto that has the information
   * @return List of PairResult object with the ids of the employees that has worked more time together.
   */
  List<PairResult> findLongestPartnersByProject(List<CompanyInformation> companyInformationList);

}
