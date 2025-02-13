package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.response.ReceiptImageResponse;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FacadeExpenseService {

    void createExpense(CreateExpenseRequest createExpenseRequest,
            CustomUserDetails customUserDetails);

    List<Expense> getExpensesByMonth(CustomUserDetails customUserDetails, int year, int month);

    List<Expense> getExpensesByDay(CustomUserDetails customUserDetails, LocalDate date);

    Expense getExpenseById(CustomUserDetails customUserDetails, Long expenseId);

    void updateExpense(Long id, UpdateExpenseRequest updateExpenseRequest,
            CustomUserDetails customUserDetails);

    void deleteExpense(Long id, CustomUserDetails customUserDetails);

    void createReceiptExpense(MultipartFile file, CreateReceiptRequest dto, CustomUserDetails customUserDetails)
            throws IOException;

    List<ReceiptImageResponse> getAllMyReceipts(int year, int month, CustomUserDetails customUserDetails);
}
