package com.walletguardians.walletguardiansapi.domain.budget.controller;

import com.walletguardians.walletguardiansapi.domain.budget.controller.dto.BudgetRequest;
import com.walletguardians.walletguardiansapi.domain.budget.controller.dto.UpdateBudgetRequest;
import com.walletguardians.walletguardiansapi.domain.budget.entity.Budget;
import com.walletguardians.walletguardiansapi.domain.budget.service.BudgetService;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.response.BaseResponse;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budget")
public class BudgetController {

  private final BudgetService budgetService;
  private final BaseResponseService baseResponseService;

  @PostMapping()
  public ResponseEntity<BaseResponse<Budget>> createBudget(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BudgetRequest budgetRequest){
    Budget budget = budgetService.createBudget(customUserDetails.getUserId(), budgetRequest);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(budget));
  }

  @GetMapping()
  public ResponseEntity<BaseResponse<Budget>> getBudget(@AuthenticationPrincipal CustomUserDetails customUserDetails){
    Budget budget = budgetService.getBudget(customUserDetails);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(budget));
  }

  @PutMapping()
  public ResponseEntity<BaseResponse<Void>> updateBudget(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody
      UpdateBudgetRequest updateBudgetRequest){
    budgetService.updateBudget(customUserDetails, updateBudgetRequest);
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse());
  }

  @DeleteMapping()
  public ResponseEntity<BaseResponse<Void>> deleteBudget(@AuthenticationPrincipal CustomUserDetails customUserDetails){
    budgetService.deleteBudget(customUserDetails);
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse());
  }

}
