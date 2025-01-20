package com.walletguardians.walletguardiansapi.expenses.controller.dto.response;

import com.walletguardians.walletguardiansapi.category.entity.Category;
import com.walletguardians.walletguardiansapi.expenses.entity.Expense;
import lombok.Builder;

public class ExpenseResponse {

    private Long id;

    private Category category;

    private int amount;

    private String storeName;

    private String description;

    @Builder
    private ExpenseResponse(Long id, Category category, int amount, String storeName, String description) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.storeName = storeName;
        this.description = description;
    }

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
