package com.walletguardians.walletguardiansapi.expenses.service;

import com.walletguardians.walletguardiansapi.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.expenses.controller.dto.response.ExpenseResponse;
import com.walletguardians.walletguardiansapi.expenses.repository.ExpenseRepository;
import com.walletguardians.walletguardiansapi.expenses.entity.Expense;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

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
}
