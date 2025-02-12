package com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.FileInfo;
import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.OcrResponse;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.global.util.OcrUtil;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateReceiptRequest {
    private LocalDate date;
    private String category;
    private String description;

    public Expense toEntity(FileInfo fileInfo, OcrResponse ocrResponse, User user) {
        return Expense.builder()
                .amount(OcrUtil.extractPrice(ocrResponse))
                .category(category)
                .description(description)
                .storeName(OcrUtil.extractStoreName(ocrResponse))
                .date(date)
                .imagePath(fileInfo.getFilePath())
                .user(user)
                .build();
    }
}
