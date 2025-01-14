package com.walletguardians.walletguardiansapi.expenses.service;

import com.walletguardians.walletguardiansapi.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.expenses.repository.ExpenseRepository;
import com.walletguardians.walletguardiansapi.expenses.entity.Expense;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public void createExpense(Date date, CreateExpenseRequest createExpenseRequest) {
        Expense expense = createExpenseRequest.toEntity();
        expense.setDate(date);
        expenseRepository.save(expense);
    }

}
