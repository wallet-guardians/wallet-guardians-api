package com.walletguardians.walletguardiansapi.domain.income.controller.dto;

import com.walletguardians.walletguardiansapi.domain.income.entity.Income;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class CreateIncomeRequest {

  private LocalDate date;
  private int amount;
  private String category;
  private String description;

  public Income toEntity(User user){
    return Income.builder()
        .amount(amount)
        .category(category)
        .description(description)
        .date(date)
        .user(user)
        .build();
  }

}
