package com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class CreateReceiptRequest implements Serializable {
    private String category;

    private String description;;
}
