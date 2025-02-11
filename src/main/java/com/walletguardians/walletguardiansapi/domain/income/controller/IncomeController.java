package com.walletguardians.walletguardiansapi.domain.income.controller;

import com.walletguardians.walletguardiansapi.domain.income.controller.dto.CreateIncomeRequest;
import com.walletguardians.walletguardiansapi.domain.income.controller.dto.UpdateIncomeRequest;
import com.walletguardians.walletguardiansapi.domain.income.entity.Income;
import com.walletguardians.walletguardiansapi.domain.income.service.IncomeService;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.response.BaseResponse;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/income")
public class IncomeController {

  private final IncomeService incomeService;
  private final BaseResponseService baseResponseService;

  @PostMapping()
  public ResponseEntity<BaseResponse<Void>> createIncome(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody CreateIncomeRequest createIncomeRequest) {
    incomeService.createIncome(createIncomeRequest, customUserDetails);
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse());
  }

  @GetMapping("/month")
  public ResponseEntity<BaseResponse<List<Income>>> getIncomeByMonth(
      @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam int year,
      @RequestParam int month) {
    List<Income> incomes = incomeService.getIncomesByMonth(customUserDetails, year, month);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(incomes));
  }

  @GetMapping("/day")
  public ResponseEntity<BaseResponse<List<Income>>> getIncomeByDay(@AuthenticationPrincipal CustomUserDetails customUserDetails,
  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
    List<Income> incomes = incomeService.getIncomeByDay(customUserDetails, date);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(incomes));
  }

  @GetMapping("/{incomeId}")
  public ResponseEntity<BaseResponse<Income>> getIncomeById(@PathVariable Long incomeId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
    Income income = incomeService.getIncomeById(customUserDetails, incomeId);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(income));
  }

  @PutMapping("/{incomeId}")
  public ResponseEntity<BaseResponse<Void>> updateIncome(@AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable Long incomeId, @RequestBody UpdateIncomeRequest updateIncomeRequest){
    incomeService.updateIncome(incomeId, updateIncomeRequest, customUserDetails);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse());
  }

  @DeleteMapping("/{incomeId}")
  public ResponseEntity<BaseResponse<Void>> deleteIncome(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long incomeId){
    incomeService.deleteIncome(customUserDetails, incomeId);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse());
  }

}
