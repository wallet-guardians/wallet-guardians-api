package com.walletguardians.walletguardiansapi.domain.income.controller.dto;

import com.walletguardians.walletguardiansapi.domain.income.entity.Income;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import java.time.LocalDate;
import jdk.jfr.Category;
import lombok.Getter;

@Getter
public class CreateIncomeRequest {

  private int amount;
  private String category;
  private String description;

  public Income toEntity(User user, LocalDate date){
    return Income.builder()
        .amount(amount)
        .category(category)
        .description(description)
        .user(user)
        .date(date)
        .build();
  }

}
