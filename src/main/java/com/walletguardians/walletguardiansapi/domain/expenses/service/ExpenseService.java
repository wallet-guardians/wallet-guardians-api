package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.repository.ExpenseRepository;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.repository.UserRepository;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.auth.cloudStorage.service.CloudStorageService;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import com.walletguardians.walletguardiansapi.global.util.TimeUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

  private final ExpenseRepository expenseRepository;
  private final CloudStorageService cloudStorageService;
  private final UserRepository userRepository;

  @Value("${spring.cloud.gcp.storage.bucket}")
  private String bucketName;

  @Transactional
  public void createExpense(CreateExpenseRequest createExpenseRequest, CustomUserDetails customUserDetails) {
    User user = userRepository.findByEmail(customUserDetails.getUsername()).orElseThrow(()-> new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR));
    createExpenseRequest.setUser(user);
    Expense expense = createExpenseRequest.toEntity();
    expense.setDate(TimeUtil.formatTime());
    expenseRepository.save(expense);
  }

  @Transactional(readOnly = true)
  public List<Expense> getMonthlyExpenses(CustomUserDetails customUserDetails, int year,
      int month) {
    Long userId = customUserDetails.getUserId();

    LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
    LocalDateTime endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth()).withHour(23)
        .withMinute(59).withSecond(59);

    return expenseRepository.findAllByUserIdAndDateBetween(userId, startOfMonth, endOfMonth);
  }

  @Transactional
  public void updateExpense(Long id, UpdateExpenseRequest updateExpenseRequest,
      CustomUserDetails customUserDetails) {
    Long userId = customUserDetails.getUserId();

    Expense findExpense = expenseRepository.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXPENSES));

    findExpense.update(updateExpenseRequest.toEntity());
  }

  @Transactional
  public void deleteExpense(Long id, CustomUserDetails customUserDetails) {
    Long userId = customUserDetails.getUserId();

    Expense findExpense = expenseRepository.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXPENSES));

    expenseRepository.delete(findExpense);
  }

  public void uploadReceipt(MultipartFile receiptFile, CreateReceiptRequest dto, String email) {
    cloudStorageService.uploadPicture(receiptFile, "receipts", dto, email, LocalDate.now());
  }

  @Transactional(readOnly = true)
  public List<Expense> getExpensesByDay(CustomUserDetails customUserDetails, LocalDate date) {
    Long userId = customUserDetails.getUserId();
    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.atTime(23, 59, 59);

    return expenseRepository.findAllByUserIdAndDateBetween(userId, startOfDay, endOfDay);
  }

  @Transactional(readOnly = true)
  public Expense getExpenseById(CustomUserDetails customUserDetails, Long expenseId) {
    Long userId = customUserDetails.getUserId();

    return expenseRepository.findByIdAndUserId(expenseId, userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXPENSES));
  }

}
