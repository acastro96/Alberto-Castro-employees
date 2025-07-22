package com.alberto.sirmatest.dto;

public record EmployeePair(int emp1, int emp2) {

  public EmployeePair {
    if (emp1 > emp2) {
      int temp = emp1;
      emp1 = emp2;
      emp2 = temp;
    }
  }

}
