package com.walletguardians.walletguardiansapi.domain.user.service;

import com.walletguardians.walletguardiansapi.domain.budget.entity.Budget;
import com.walletguardians.walletguardiansapi.domain.budget.repository.BudgetRepository;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.domain.expenses.repository.ExpenseRepository;
import com.walletguardians.walletguardiansapi.domain.user.controller.dto.request.UpdateUserRequest;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.entity.auth.ExpenseCategory;
import com.walletguardians.walletguardiansapi.domain.user.repository.UserRepository;
import com.walletguardians.walletguardiansapi.global.auth.jwt.service.JwtService;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final ExpenseRepository expenseRepository;
  private final BudgetRepository budgetRepository;

  @Transactional(readOnly = true)
  public User findUserByUserId(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID));
  }

  @Transactional(readOnly = true)
  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID));
  }

  @Transactional
  public void logout(String accessToken, String email) {
    boolean expiration = jwtService.validateToken(accessToken);
    if (expiration) {
      jwtService.deleteRefreshTokenByEmail(email);
    } else {
      throw new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID);
    }
  }

  @Transactional
  public void deleteById(Long userId) {
    userRepository.deleteById(userId);
  }

  @Transactional
  public User updatePassword(Long userId, UpdateUserRequest updateUserRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID));
    validatePassword(user, updateUserRequest);
    user.updatePassword(passwordEncoder, updateUserRequest.getNewPassword());
    return user;
  }

  private void validatePassword(User user, UpdateUserRequest updateUserRequest) {
    if (!user.isPasswordValid(passwordEncoder, updateUserRequest.getPassword())) {
      throw new BaseException(BaseResponseStatus.UNAUTHORIZED);
    }
    if (user.isPasswordValid(passwordEncoder, updateUserRequest.getNewPassword())) {
      throw new BaseException(BaseResponseStatus.SAME_PASSWORD);
    }
  }

  @Scheduled(cron = "0 0 0 1 * ?") // 매월 1일 자정에 실행
  @Transactional
  public void updateUserDefense() {
    LocalDate lastMonth = LocalDate.now().minusMonths(1);
    List<User> users = userRepository.findAll();
    users.forEach(user -> updateDefenseForUser(user, lastMonth));
  }

  private void updateDefenseForUser(User user, LocalDate lastMonth) {
    int totalSpent = expenseRepository.getTotalSpentByUserIdAndMonth(user.getId(), lastMonth);
    Integer budget = budgetRepository.getBudgetByUserIdAndMonth(user.getId(), lastMonth);
    int budgetAmount = (budget != null) ? budget : 0;

    if (totalSpent > budgetAmount) {
      user.decreaseDefense(6);
    } else if (totalSpent < budgetAmount) {
      user.increaseDefense(10);
    }
  }

  public void updateMonthlyTitles(User user) {
    LocalDate now = LocalDate.now();
    YearMonth lastMonth = YearMonth.from(now).minusMonths(1);

    List<Expense> lastMonthExpenses = user.getExpenses().stream()
        .filter(e -> YearMonth.from(e.getDate()).equals(lastMonth))
        .toList();

    String newBudgetTitle = determineBudgetTitle(lastMonthExpenses, user.getBudget());
    String newExpenseTitle = determineExpenseTitle(lastMonthExpenses);

    user.updateBudgetTitle(newBudgetTitle);
    user.updateExpenseTitle(newExpenseTitle);
  }

  private String determineBudgetTitle(List<Expense> lastMonthExpenses, Budget budget) {
    double totalExpense = lastMonthExpenses.stream().mapToDouble(Expense::getAmount).sum();

    double budgetLimit = budget.getAmount();
    double budgetUsageRate = totalExpense / budgetLimit;

    if (totalExpense == 0) {
      return "무소유: 이게 가능해?";
    }
    if (budgetUsageRate > 1.0) {
      return "가계부 파괴자";
    }
    if (budgetUsageRate >= 0.9) {
      return "신에게는 아직 예산이 남아있습니다";
    }
    if (budgetUsageRate >= 0.5) {
      return "다음은 너다";
    }
    if (budgetUsageRate > 0) {
      return "지갑 속은 안전해";
    }

    return null;
  }

  private String determineExpenseTitle(List<Expense> lastMonthExpenses) {
    // 📌 이번 달 소비 횟수 카운트
    Map<ExpenseCategory, Long> categoryCount = lastMonthExpenses.stream()
        .collect(Collectors.groupingBy(
            e -> ExpenseCategory.fromString(e.getCategory()),
            Collectors.counting()
        ));

    if (categoryCount.size() == 1) {
      return "한 우물만 파는 중";
    }

    // 📌 소비 횟수가 많은 카테고리 정렬 (내림차순)
    List<Map.Entry<ExpenseCategory, Long>> sortedCategories = categoryCount.entrySet().stream()
        .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
        .toList();

    // 📌 가장 높은 기준 충족하는 칭호 찾기
    for (Map.Entry<ExpenseCategory, Long> entry : sortedCategories) {
      ExpenseCategory category = entry.getKey();
      long count = entry.getValue();

      for (com.walletguardians.walletguardiansapi.domain.user.valueobject.TitleCondition condition : category.getTitles()) {
        if (count >= condition.getThreshold()) {
          return condition.getTitle();
        }
      }
    }

    return null;
  }

}
