package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.response.ExpenseResponse;
import com.walletguardians.walletguardiansapi.domain.expenses.repository.ExpenseRepository;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final Storage storage;

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

    public void uploadReceipt (MultipartFile receiptFile, CreateReceiptRequest dto) {
        if (receiptFile.isEmpty()) {
             throw new IllegalArgumentException("receipt file is empty");
        }
        String uniqueFileName = UUID.randomUUID().toString() + "_" + receiptFile.getOriginalFilename();
        String ext = receiptFile.getContentType();

        BlobId blobId = BlobId.of(bucketName, uniqueFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(ext).build();

        try (WriteChannel writer = storage.writer(blobInfo)) {
            byte[] imageData = receiptFile.getBytes();
            writer.write(ByteBuffer.wrap(imageData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
