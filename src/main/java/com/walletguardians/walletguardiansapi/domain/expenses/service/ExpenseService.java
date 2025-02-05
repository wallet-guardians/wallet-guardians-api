package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.google.cloud.storage.Storage;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.response.ExpenseResponse;
import com.walletguardians.walletguardiansapi.domain.expenses.repository.ExpenseRepository;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.global.auth.cloudStorage.service.CloudStorageService;
import java.text.SimpleDateFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final Storage storage;
    private final CloudStorageService cloudStorageService;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public void createExpense(Date date, CreateExpenseRequest createExpenseRequest) {
        Expense expense = createExpenseRequest.toEntity();
        expense.setDate(date);
        expenseRepository.save(expense);
    }
  
    public List<ExpenseResponse> getExpenses(Date date) {
        List<Expense> expenses = expenseRepository.findAll();
        List<ExpenseResponse> expenseResponses = new ArrayList<>();
        for (Expense expense : expenses) {
            if (expense.getDate().equals(date)) {
                expenseResponses.add(ExpenseResponse.from(expense));
            }
        }
        return expenseResponses;
    }
  
    public void updateExpense(Long id, UpdateExpenseRequest updateExpenseRequest) {
        Expense updateExpense = updateExpenseRequest.toEntity();
        Expense findExpense = expenseRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("Expense not found"));
        findExpense.update(updateExpense);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public void uploadReceipt (MultipartFile receiptFile, CreateReceiptRequest dto, String email, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        cloudStorageService.uploadPicture(receiptFile, "receipts", dto, email, formattedDate);
    }

}
