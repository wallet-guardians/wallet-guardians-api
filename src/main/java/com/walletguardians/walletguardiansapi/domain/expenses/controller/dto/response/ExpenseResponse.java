package com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.response;

import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {

  private Long id;

  private LocalDateTime date;

  private String category;

  private int amount;

  private String storeName;

  private String description;

  public static ExpenseResponse from(Expense expense) {
    return ExpenseResponse.builder()
        .id(expense.getId())
        .category(expense.getCategory())
        .amount(expense.getAmount())
        .storeName(expense.getStoreName())
        .description(expense.getDescription())
        .build();
  }
}
