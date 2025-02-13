package com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.response;

import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptImageResponse {

    private Long id;

    private LocalDate date;

    private String imageUrl;

    public static ReceiptImageResponse from(Expense expense) {
        return ReceiptImageResponse.builder()
                .id(expense.getId())
                .date(expense.getDate())
                .imageUrl(expense.getImagePath())
                .build();
    }
}
