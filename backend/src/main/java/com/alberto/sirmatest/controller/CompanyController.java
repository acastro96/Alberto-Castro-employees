package com.alberto.sirmatest.controller;

import com.alberto.sirmatest.dto.CompanyInformation;
import com.alberto.sirmatest.dto.PairResult;
import com.alberto.sirmatest.exception.CompanyException;
import com.alberto.sirmatest.service.contract.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CompanyController {

  private final CompanyService companyService;
  private static final Logger log = LoggerFactory.getLogger(CompanyController.class);

  public CompanyController(CompanyService companyService){
    this.companyService = companyService;
  }

  @PostMapping("employee/calculate-time-together")
  @Operation(summary = "Calculate the employees that has worked more time together in a project")
  @ApiResponse(responseCode = "200", description = "Success operation")
  @ApiResponse(responseCode = "204", description = "Not found")
  @ApiResponse(responseCode = "400", description = "Business Error")
  public ResponseEntity<List<PairResult>> calculateTimePairEmployees(@RequestBody(required = false) @Valid List<CompanyInformation> companyInformation){

    if (companyInformation == null || companyInformation.isEmpty()) {
      log.error("Request body is empty");
      throw new CompanyException("Request body is required and cannot be empty");
    }

    log.info("Received request with {} companyInformation entries", companyInformation.size());

    List<PairResult> pairResults = companyService.findLongestPartnersByProject(companyInformation);

    if(pairResults.isEmpty()){
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(pairResults);
  }

}
