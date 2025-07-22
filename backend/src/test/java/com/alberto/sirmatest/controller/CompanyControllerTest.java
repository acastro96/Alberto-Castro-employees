package com.alberto.sirmatest.controller;

import com.alberto.sirmatest.dto.PairResult;
import com.alberto.sirmatest.service.contract.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CompanyService companyService;

  @Test
  void shouldReturn200WhenPairFound() throws Exception {
    List<PairResult> mockResult = List.of(
        new PairResult(1, 2, 30, List.of(100))
    );
    when(companyService.findLongestPartnersByProject(any())).thenReturn(mockResult);

    String jsonInput = """
          [
            { "empId": 1, "projectId": 100, "dateFrom": "2022-01-01", "dateTo": "2022-04-01" },
            { "empId": 2, "projectId": 100, "dateFrom": "2022-02-01", "dateTo": "2022-05-01" }
          ]
        """;

    mockMvc.perform(post("/api/employee/calculate-time-together")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonInput))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].emp1Id").value(1))
        .andExpect(jsonPath("$[0].emp2Id").value(2))
        .andExpect(jsonPath("$[0].totalDays").value(30));
  }

  @Test
  void shouldReturn204WhenNoResult() throws Exception {
    when(companyService.findLongestPartnersByProject(any())).thenReturn(List.of());

    mockMvc.perform(post("/api/employee/calculate-time-together")
            .contentType(MediaType.APPLICATION_JSON)
            .content("[]"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Request body is required and cannot be empty"));
  }

}
