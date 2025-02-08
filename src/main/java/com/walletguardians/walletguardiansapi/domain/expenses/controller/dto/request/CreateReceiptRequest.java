package com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request;
import java.time.LocalDate;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class CreateReceiptRequest {
    private LocalDate date;
    private String category;
    private String description;
}
