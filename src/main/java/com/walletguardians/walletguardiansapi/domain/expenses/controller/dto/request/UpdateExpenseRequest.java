package com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request;

import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import lombok.Getter;

@Getter
public class UpdateExpenseRequest {

  private String category;
  private int amount;
  private String storeName;
  private String description;

  public Expense toEntity() {
    return Expense.builder()
        .category(category)
        .amount(amount)
        .storeName(storeName)
        .description(description)
        .build();
  }
}
