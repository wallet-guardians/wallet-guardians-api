package com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateExpenseResponse {
    private boolean success;

    @Builder
    private CreateExpenseResponse(boolean success) {
        this.success = success;
    }


}
