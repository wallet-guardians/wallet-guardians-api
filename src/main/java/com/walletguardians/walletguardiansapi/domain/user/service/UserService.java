package com.walletguardians.walletguardiansapi.domain.user.service;

import com.walletguardians.walletguardiansapi.domain.budget.repository.BudgetRepository;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.domain.expenses.repository.ExpenseRepository;
import com.walletguardians.walletguardiansapi.domain.user.controller.dto.request.UpdateUserRequest;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.entity.auth.ExpenseCategory;
import com.walletguardians.walletguardiansapi.domain.user.repository.UserRepository;
import com.walletguardians.walletguardiansapi.domain.user.valueobject.TitleCondition;
import com.walletguardians.walletguardiansapi.global.auth.jwt.service.JwtService;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
  public void updateUserDefenseAndTitles() {
    LocalDate lastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
    List<User> users = userRepository.findAll();
    users.forEach(user -> {
      updateDefenseForUser(user, lastMonth);
      updateTitles(user, lastMonth);
    });
  }

  private void updateDefenseForUser(User user, LocalDate lastMonth) {
    int totalSpent = expenseRepository.getTotalSpentByUserIdAndMonth(user.getId(), lastMonth);
    int budgetAmount = budgetRepository.getBudgetByUserIdAndMonth(user.getId(), lastMonth)
        .orElse(0);

    if (totalSpent > budgetAmount) {
      user.decreaseDefense(6);
    } else if (totalSpent < budgetAmount) {
      user.increaseDefense(10);
    }
  }

  private void updateTitles(User user, LocalDate firstOfLastMonth) {
    LocalDate lastOfLastMonth = firstOfLastMonth.with(TemporalAdjusters.lastDayOfMonth());
    Long userId = user.getId();

    int totalSpent = expenseRepository.getTotalSpentByUserIdAndMonth(userId, firstOfLastMonth);
    int lastMonthBudget = budgetRepository.getBudgetByUserIdAndMonth(userId, firstOfLastMonth)
        .orElse(0);

    List<Expense> lastMonthExpenses = expenseRepository.findAllByUserIdAndDateBetweenOrderByDateAscIdAsc(
        userId, firstOfLastMonth, lastOfLastMonth);

    user.updateBudgetTitle(determineBudgetTitle(totalSpent, lastMonthBudget));
    user.updateExpenseTitle(determineExpenseTitle(lastMonthExpenses));
    userRepository.save(user);
  }

  private String determineBudgetTitle(int totalSpent, int budget) {

    if (budget == 0) {
      return null;
    }

    double budgetUsageRate = (double) totalSpent / budget;

    if (budgetUsageRate == 0) {
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
    return "지갑 속은 안전해";
  }

  private String determineExpenseTitle(List<Expense> lastMonthExpenses) {
    if (lastMonthExpenses.isEmpty()) {
      return null;
    }

    Map<ExpenseCategory, Long> categoryCount = lastMonthExpenses.stream()
        .collect(Collectors.groupingBy(
            e -> {
              ExpenseCategory category = ExpenseCategory.fromString(
                  e.getCategory().trim());
              return category != null ? category : ExpenseCategory.ETC; // 매칭 실패 시 기타(ETC)로 처리
            },
            Collectors.counting()
        ));

    if (categoryCount.size() == 1) {
      return "한 우물만 파는 중";
    }

    return categoryCount.entrySet().stream()
        .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
        .flatMap(entry -> entry.getKey().getTitles().stream()
            .filter(condition -> entry.getValue() >= condition.getThreshold())
            .map(TitleCondition::getTitle))
        .findFirst()
        .orElse(null);
  }

}
