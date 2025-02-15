package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.response.ReceiptImageResponse;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.FileInfo;
import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.OcrResponse;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeExpenseServiceImpl implements FacadeExpenseService {

    private static final String PICTURE_TYPE = "receipts";

    private final ExpenseService expenseService;
    private final OcrService ocrService;
    private final CloudStorageService cloudStorageService;

    @Transactional
    @Override
    public void createExpense(CreateExpenseRequest createExpenseRequest,
            CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        expenseService.createExpense(user, createExpenseRequest);
    }

    @Override
    public List<Expense> getExpensesByMonth(CustomUserDetails customUserDetails, int year,
            int month) {
        Long userId = customUserDetails.getUserId();
        return expenseService.getExpensesByMonth(userId, year, month);
    }

    @Override
    public List<Expense> getExpensesByDay(CustomUserDetails customUserDetails, LocalDate date) {
        Long userId = customUserDetails.getUserId();
        return expenseService.getExpensesByDay(userId, date);
    }

    @Override
    public Expense getExpenseById(CustomUserDetails customUserDetails, Long expenseId) {
        Long userId = customUserDetails.getUserId();
        return expenseService.getExpenseById(userId, expenseId);
    }

    @Transactional
    @Override
    public void updateExpense(Long expenseId, UpdateExpenseRequest updateExpenseRequest,
            CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        Expense findExpense = expenseService.getExpenseByIdAndUserId(userId, expenseId);
        expenseService.updateExpense(findExpense, updateExpenseRequest);
    }

    @Transactional
    @Override
    public void deleteExpense(Long expenseId, CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        Expense findExpense = expenseService.getExpenseByIdAndUserId(userId, expenseId);
        expenseService.deleteExpense(findExpense);
    }

    @Transactional
    @Override
    public void createReceiptExpense(MultipartFile file, CreateReceiptRequest dto, CustomUserDetails customUserDetails)
            throws IOException {
        User user = customUserDetails.getUser();
        FileInfo fileInfo = cloudStorageService.uploadPicture(file, PICTURE_TYPE, dto, customUserDetails);
        OcrResponse ocrResponse = ocrService.sendOcrRequest(fileInfo.getContentType(), fileInfo.getFilePath());
        expenseService.createReceiptExpense(fileInfo, ocrResponse, dto, user);
    }

    @Override
    public List<ReceiptImageResponse> getAllMyReceipts(int year, int month,
            CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        List<Expense> expenses = expenseService.getExpensesByMonth(userId, year, month);
        expenses.removeIf(expense -> expense.getImagePath()==null || expense.getImagePath().isEmpty());
        return expenses.stream()
                .map(ReceiptImageResponse::from)
                .toList();
    }
}
