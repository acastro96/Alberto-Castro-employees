package com.alberto.sirmatest.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Utility {

  private Utility() {
  }

  public static LocalDate parse(String dateStr) {
    if (dateStr == null || dateStr.equalsIgnoreCase("NULL")) {
      return LocalDate.now();
    }
    return LocalDate.parse(dateStr);
  }

  public static long getOverlapDays(String from1, String to1, String from2, String to2) {
    LocalDate start1 = parse(from1);
    LocalDate end1 = parse(to1);
    LocalDate start2 = parse(from2);
    LocalDate end2 = parse(to2);

    LocalDate overlapStart = start1.isAfter(start2) ? start1 : start2;
    LocalDate overlapEnd = end1.isBefore(end2) ? end1 : end2;

    if (overlapStart.isAfter(overlapEnd)) return 0;

    return ChronoUnit.DAYS.between(overlapStart, overlapEnd) + 1;
  }

}
