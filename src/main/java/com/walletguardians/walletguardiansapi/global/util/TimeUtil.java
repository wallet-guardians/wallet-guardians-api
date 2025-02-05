package com.walletguardians.walletguardiansapi.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimeUtil {

  private TimeUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static LocalDateTime formatTime(){
    return LocalDate.now().atStartOfDay();
  }
}
