package com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request;

import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;

import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateExpenseRequest {

  private int amount;

  private String category;

  private String description;

  private String storeName;

  private User user;

  public Expense toEntity() {
    return Expense.builder()
        .amount(amount)
        .category(category)
        .description(description)
        .storeName(storeName)
        .user(user)
        .build();
  }

}
