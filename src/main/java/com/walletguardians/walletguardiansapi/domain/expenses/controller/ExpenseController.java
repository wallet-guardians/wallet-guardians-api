package com.walletguardians.walletguardiansapi.domain.expenses.controller;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.domain.expenses.service.ExpenseService;
import com.walletguardians.walletguardiansapi.domain.user.service.UserService;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.response.BaseResponse;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {

  private final ExpenseService expenseService;
  private final UserService userService;
  private final BaseResponseService baseResponseService;

  @PostMapping("now")
  public ResponseEntity<BaseResponse<Void>> createExpense(
      @RequestBody CreateExpenseRequest createExpenseRequest,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    expenseService.createExpense(createExpenseRequest, customUserDetails);
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse());
  }

  @GetMapping("/monthly")
  public ResponseEntity<BaseResponse<List<Expense>>> getMonthlyExpenses(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestParam int year,
      @RequestParam int month
  ) {
    List<Expense> expenses = expenseService.getMonthlyExpenses(customUserDetails, year, month);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(expenses));
  }

  @GetMapping("/day")
  public ResponseEntity<BaseResponse<List<Expense>>> getExpensesByDay(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

    List<Expense> expenses = expenseService.getExpensesByDay(customUserDetails, date);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(expenses));
  }

  @GetMapping("/{expenseId}")
  public ResponseEntity<BaseResponse<Expense>> getExpenseById(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable Long expenseId) {
    Expense expense = expenseService.getExpenseById(customUserDetails, expenseId);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(expense));
  }

  @DeleteMapping("/{id}")
  public void deleteExpense(@PathVariable Long id,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    expenseService.deleteExpense(id, customUserDetails);
  }

  @PostMapping("/receipt")
  public ResponseEntity<BaseResponse<Void>> saveFile(
      @RequestPart(value = "file") MultipartFile file,
      @RequestPart(value = "info") CreateReceiptRequest dto,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    expenseService.uploadReceipt(file, dto, customUserDetails.getUsername());
    // expenseService.createReceiptExpense(file, dto);

    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse());
  }

  @PutMapping("/{expenseId}")
  public ResponseEntity<BaseResponse<Void>> updateExpense(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable Long expenseId,
      @RequestBody UpdateExpenseRequest updateExpenseRequest) {

    expenseService.updateExpense(expenseId, updateExpenseRequest, customUserDetails);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse());
  }

}
