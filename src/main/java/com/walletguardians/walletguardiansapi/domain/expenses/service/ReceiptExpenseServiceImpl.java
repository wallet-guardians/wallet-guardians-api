package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReceiptExpenseServiceImpl implements ReceiptExpenseService {

    private final CloudStorageService cloudStorageService;

    private final OcrService ocrService;

    @Override
    @Transactional
    public void uploadReceipt(
            MultipartFile receiptFile, CreateReceiptRequest dto, CustomUserDetails customUserDetails) {
        cloudStorageService.uploadPicture(receiptFile, "receipts", dto, customUserDetails);
    }

    @Override
    @Transactional
    public void createReceiptExpense(MultipartFile file, CreateReceiptRequest dto)
            throws IOException {
        File tempFile = File.createTempFile("temp", file.getOriginalFilename());
        file.transferTo(tempFile);

        List<String> result = ocrService.sendOcrRequest("POST", tempFile.getPath(), file.getContentType());
    }
}
