package com.walletguardians.walletguardiansapi.domain.income.controller.dto;

import com.walletguardians.walletguardiansapi.domain.income.entity.Income;
import lombok.Getter;

@Getter
public class UpdateIncomeRequest {

  private String category;
  private int amount;
  private String description;

  public Income toEntity() {
    return Income.builder()
        .category(category)
        .amount(amount)
        .description(description)
        .build();
  }
}
