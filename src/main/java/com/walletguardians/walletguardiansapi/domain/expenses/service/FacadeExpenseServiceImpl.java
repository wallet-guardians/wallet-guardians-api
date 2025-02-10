package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
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

    private final ExpenseService expenseService;
    private final ReceiptExpenseService receiptExpenseService;
    private final ReceiptExpenseServiceImpl receiptExpenseServiceImpl;


    @Transactional
    @Override
    public void createExpense(CreateExpenseRequest createExpenseRequest,
            CustomUserDetails customUserDetails) {
        expenseService.createExpense(createExpenseRequest, customUserDetails);
    }

    @Override
    public List<Expense> getExpensesByMonth(CustomUserDetails customUserDetails, int year,
            int month) {
        return expenseService.getExpensesByMonth(customUserDetails, year, month);
    }

    @Override
    public List<Expense> getExpensesByDay(CustomUserDetails customUserDetails, LocalDate date) {
        return expenseService.getExpensesByDay(customUserDetails, date);
    }

    @Override
    public Expense getExpenseById(CustomUserDetails customUserDetails, Long expenseId) {
        return expenseService.getExpenseById(customUserDetails, expenseId);
    }

    @Transactional
    @Override
    public void updateExpense(Long id, UpdateExpenseRequest updateExpenseRequest,
            CustomUserDetails customUserDetails) {
        expenseService.updateExpense(id, updateExpenseRequest, customUserDetails);
    }

    @Transactional
    @Override
    public void deleteExpense(Long id, CustomUserDetails customUserDetails) {
        expenseService.deleteExpense(id, customUserDetails);
    }

    @Transactional
    @Override
    public void createReceiptExpense(MultipartFile file, CreateReceiptRequest dto, CustomUserDetails customUserDetails)
            throws IOException {
        receiptExpenseService.uploadReceipt(file, dto, customUserDetails);
        receiptExpenseService.createReceiptExpense(file, dto);
    }
}
