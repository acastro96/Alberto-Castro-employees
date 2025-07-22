package com.alberto.sirmatest.dto;

import java.util.List;

public record PairResult(int emp1Id, int emp2Id, long totalDays, List<Integer> commonProjects) {
}
