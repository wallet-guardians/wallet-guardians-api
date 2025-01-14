package com.walletguardians.walletguardiansapi.expenses.controller;

import com.walletguardians.walletguardiansapi.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.expenses.service.ExpenseService;
import com.walletguardians.walletguardiansapi.expenses.controller.dto.response.ExpenseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Date;

@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping("/{date}")
    public void createExpense(@RequestBody CreateExpenseRequest createExpenseRequest, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        expenseService.createExpense(date, createExpenseRequest);
    }

    @GetMapping("/{date}")
    public List<ExpenseResponse> getMember(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return expenseService.getExpenses(date);
    }
}
