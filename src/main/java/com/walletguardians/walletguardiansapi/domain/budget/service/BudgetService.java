package com.walletguardians.walletguardiansapi.domain.budget.service;

import com.walletguardians.walletguardiansapi.domain.budget.controller.dto.BudgetRequest;
import com.walletguardians.walletguardiansapi.domain.budget.controller.dto.UpdateBudgetRequest;
import com.walletguardians.walletguardiansapi.domain.budget.repository.BudgetRepository;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.budget.entity.Budget;
import com.walletguardians.walletguardiansapi.domain.user.repository.UserRepository;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetService {

  private final UserRepository userRepository;
  private final BudgetRepository budgetRepository;

  @Transactional
  public Budget createBudget(Long userId, BudgetRequest budgetRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.EXIST_BUDGET));
    Budget budget = budgetRequest.toEntity();
    budget.setUser(user);
    budgetRepository.save(budget);
    return budget;
  }

  @Transactional(readOnly = true)
  public Budget getBudget(CustomUserDetails customUserDetails) {
    return budgetRepository.findByUser(customUserDetails.getUser())
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_BUDGET));
  }

  @Transactional
  public void updateBudget(CustomUserDetails customUserDetails,
      UpdateBudgetRequest updateBudgetRequest) {
    Budget budget = budgetRepository.findByUser(customUserDetails.getUser())
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_BUDGET));
    budget.updateBudget(updateBudgetRequest.getAmount());
  }

  @Transactional
  public void deleteBudget(CustomUserDetails customUserDetails) {
    Budget findBudget = budgetRepository.findByUser(customUserDetails.getUser())
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_BUDGET));
    budgetRepository.delete(findBudget);
  }

}
