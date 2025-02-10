package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    void createExpense(CreateExpenseRequest createExpenseRequest,
            CustomUserDetails customUserDetails);

    List<Expense> getExpensesByMonth(CustomUserDetails customUserDetails, int year, int month);

    List<Expense> getExpensesByDay(CustomUserDetails customUserDetails, LocalDate date);

    Expense getExpenseById(CustomUserDetails customUserDetails, Long expenseId);

    void updateExpense(Long id, UpdateExpenseRequest updateExpenseRequest,
            CustomUserDetails customUserDetails);

    void deleteExpense(Long id, CustomUserDetails customUserDetails);
}
