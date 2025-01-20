package com.walletguardians.walletguardiansapi.expenses.service;

import com.walletguardians.walletguardiansapi.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.expenses.controller.dto.response.ExpenseResponse;
import com.walletguardians.walletguardiansapi.expenses.repository.ExpenseRepository;
import com.walletguardians.walletguardiansapi.expenses.entity.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    // 지출 생성
    public void createExpense(Date date, CreateExpenseRequest createExpenseRequest) {
        Expense expense = createExpenseRequest.toEntity();
        expense.setDate(date);
        expenseRepository.save(expense);
    }

    // 지출 조회
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

    // 지출 수정
    public void updateExpense(Long id, UpdateExpenseRequest updateExpenseRequest) {
        Expense updateExpense = updateExpenseRequest.toEntity();
        Expense findExpense = expenseRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("Expense not found"));
        findExpense.update(updateExpense);
    }

    // ID로 지출 삭제
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
