package com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
public class CreateReceiptDTO implements Serializable {
    private String category;

    private String description;;
}
