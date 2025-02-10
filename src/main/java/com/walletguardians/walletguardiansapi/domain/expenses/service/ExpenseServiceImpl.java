package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.repository.ExpenseRepository;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseServiceImpl implements ExpenseService {

  private final ExpenseRepository expenseRepository;

  @Value("${spring.cloud.gcp.storage.bucket}")
  private String bucketName;

  @Override
  @Transactional
  public void createExpense(CreateExpenseRequest createExpenseRequest,
      CustomUserDetails customUserDetails) {
    User user = customUserDetails.getUser();
    Expense expense = createExpenseRequest.toEntity(user);
    expenseRepository.save(expense);
  }

  @Override
  public List<Expense> getExpensesByMonth(CustomUserDetails customUserDetails, int year,
      int month) {
    Long userId = customUserDetails.getUserId();

    LocalDate startOfMonth = LocalDate.of(year, month, 1);
    LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

    return expenseRepository.findAllByUserIdAndDateBetweenOrderByDateAscIdAsc(userId, startOfMonth, endOfMonth);
  }

  @Override
  public List<Expense> getExpensesByDay(CustomUserDetails customUserDetails, LocalDate date) {
    Long userId = customUserDetails.getUserId();
    return expenseRepository.findAllByUserIdAndDateBetweenOrderByDateAscIdAsc(userId, date, date);
  }

  @Override
  public Expense getExpenseById(CustomUserDetails customUserDetails, Long expenseId) {
    Long userId = customUserDetails.getUserId();

    return expenseRepository.findByIdAndUserId(expenseId, userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXPENSES));
  }

  @Override
  @Transactional
  public void updateExpense(Long id, UpdateExpenseRequest updateExpenseRequest,
      CustomUserDetails customUserDetails) {
    Long userId = customUserDetails.getUserId();

    Expense findExpense = expenseRepository.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXPENSES));

    findExpense.update(updateExpenseRequest.toEntity());
  }

  @Override
  @Transactional
  public void deleteExpense(Long id, CustomUserDetails customUserDetails) {
    Long userId = customUserDetails.getUserId();

    Expense findExpense = expenseRepository.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXPENSES));

    expenseRepository.delete(findExpense);
  }
}
