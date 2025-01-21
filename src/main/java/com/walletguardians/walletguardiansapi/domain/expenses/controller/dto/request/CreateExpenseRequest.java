package com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request;

import com.walletguardians.walletguardiansapi.domain.category.entity.Category;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import lombok.Getter;

@Getter
public class CreateExpenseRequest {

    private int amount;

    private Category category;

    private String description;

    private String storeName;

    public Expense toEntity() {
        return Expense.builder()
                .amount(amount)
                .category(category)
                .description(description)
                .storeName(storeName)
                .build();
    }

}
