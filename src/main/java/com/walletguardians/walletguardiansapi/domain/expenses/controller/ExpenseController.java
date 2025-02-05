package com.walletguardians.walletguardiansapi.domain.expenses.controller;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.service.ExpenseService;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.response.ExpenseResponse;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;
import java.util.Date;

@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpenseController {

  private final ExpenseService expenseService;

  @PostMapping("/{date}")
  public void createExpense(@RequestBody CreateExpenseRequest createExpenseRequest,
      @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
    expenseService.createExpense(date, createExpenseRequest);
  }

  @GetMapping("/{date}")
  public List<ExpenseResponse> getMember(
      @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
    return expenseService.getExpenses(date);
  }

  @PutMapping("/{id}")
  public void updateExpense(@PathVariable Long id,
      @RequestBody UpdateExpenseRequest updateExpenseRequest) {
    expenseService.updateExpense(id, updateExpenseRequest);
  }

  @DeleteMapping("/{id}")
  public void deleteExpense(@PathVariable Long id) {
    expenseService.deleteExpense(id);
  }

  @PostMapping("/receipt/{date}")
  public void saveFile(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
      @RequestPart(value = "file") MultipartFile file,
      @RequestPart(value = "info") CreateReceiptRequest dto,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    expenseService.uploadReceipt(file, dto, customUserDetails.getUsername(), date);
    // expenseService.createReceiptExpense(file, dto);
  }

}
