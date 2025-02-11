package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.domain.expenses.repository.ExpenseRepository;
import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.FileInfo;
import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.OcrResponse;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReceiptExpenseServiceImpl implements ReceiptExpenseService {
    private static final String PICTURE_TYPE = "receipts";

    private final CloudStorageServiceImpl cloudStorageService;
    private final OcrService ocrService;
    private final ExpenseRepository expenseRepository;

    @Override
    @Transactional
    public FileInfo uploadReceipt(
            MultipartFile receiptFile, CreateReceiptRequest dto, CustomUserDetails customUserDetails) {
        return cloudStorageService.uploadPicture(receiptFile, PICTURE_TYPE, dto, customUserDetails);
    }

    @Override
    @Transactional
    public void createReceiptExpense(FileInfo fileInfo, OcrResponse ocrResponse, CreateReceiptRequest createReceiptRequest, User user)
            throws IOException {
        Expense expense = createReceiptRequest.toEntity(fileInfo, ocrResponse, user);
        expenseRepository.save(expense);
    }
}
