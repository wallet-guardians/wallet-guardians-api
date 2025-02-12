package com.walletguardians.walletguardiansapi.domain.expenses.controller;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.domain.expenses.service.FacadeExpenseService;
import com.walletguardians.walletguardiansapi.global.security.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.response.BaseResponse;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseService;
import java.io.IOException;
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

  private final FacadeExpenseService facadeExpenseService;
  private final BaseResponseService baseResponseService;

  @PostMapping()
  public ResponseEntity<BaseResponse<Void>> createExpense(
      @RequestBody CreateExpenseRequest createExpenseRequest,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    facadeExpenseService.createExpense(createExpenseRequest, customUserDetails);
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse());
  }

  @GetMapping("/month")
  public ResponseEntity<BaseResponse<List<Expense>>> getExpensesByMonth(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestParam int year,
      @RequestParam int month
  ) {
    List<Expense> expenses = facadeExpenseService.getExpensesByMonth(customUserDetails, year, month);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(expenses));
  }

  @GetMapping("/day")
  public ResponseEntity<BaseResponse<List<Expense>>> getExpensesByDay(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

    List<Expense> expenses = facadeExpenseService.getExpensesByDay(customUserDetails, date);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(expenses));
  }

  @GetMapping("/{expenseId}")
  public ResponseEntity<BaseResponse<Expense>> getExpenseById(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable Long expenseId) {
    Expense expense = facadeExpenseService.getExpenseById(customUserDetails, expenseId);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(expense));
  }

  @DeleteMapping("/{expenseId}")
  public ResponseEntity<BaseResponse<Void>> deleteExpense(@PathVariable Long expenseId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    facadeExpenseService.deleteExpense(expenseId, customUserDetails);
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse());
  }

  @PostMapping("/receipt")
  public ResponseEntity<BaseResponse<Void>> createReceiptExpense(
      @RequestPart(value = "file") MultipartFile file,
      @RequestPart(value = "info") CreateReceiptRequest dto,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
    facadeExpenseService.createReceiptExpense(file, dto, customUserDetails);

    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse());
  }

  @PutMapping("/{expenseId}")
  public ResponseEntity<BaseResponse<Void>> updateExpense(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable Long expenseId,
      @RequestBody UpdateExpenseRequest updateExpenseRequest) {

    facadeExpenseService.updateExpense(expenseId, updateExpenseRequest, customUserDetails);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse());
  }

}
