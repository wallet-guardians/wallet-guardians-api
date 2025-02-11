package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    void createExpense(User user, CreateExpenseRequest createExpenseRequest);

    List<Expense> getExpensesByMonth(Long userId, int year, int month);

    List<Expense> getExpensesByDay(Long userId, LocalDate date);

    Expense getExpenseById(Long userId, Long expenseId);

    Expense getExpenseByIdAndUserId(Long userId, Long expenseId);

    void updateExpense(Expense findExpense, UpdateExpenseRequest updateExpenseRequest);

    void deleteExpense(Expense findExpense);
}
