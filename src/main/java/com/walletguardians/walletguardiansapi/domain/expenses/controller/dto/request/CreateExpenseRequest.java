package com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request;

import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;

import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateExpenseRequest {

  private int amount;

  private LocalDate date;

  private String category;

  private String description;

  private String storeName;

  public Expense toEntity(User user) {
    return Expense.builder()
        .amount(amount)
        .category(category)
        .description(description)
        .storeName(storeName)
        .date(date)
        .user(user)
        .build();
  }

}
