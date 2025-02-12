package com.walletguardians.walletguardiansapi.domain.income.service;

import com.walletguardians.walletguardiansapi.domain.income.controller.dto.CreateIncomeRequest;
import com.walletguardians.walletguardiansapi.domain.income.controller.dto.UpdateIncomeRequest;
import com.walletguardians.walletguardiansapi.domain.income.entity.Income;
import com.walletguardians.walletguardiansapi.domain.income.repository.IncomeRepository;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.global.security.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IncomeService {

  private final IncomeRepository incomeRepository;

  @Transactional
  public void createIncome(CreateIncomeRequest createIncomeRequest,
      CustomUserDetails customUserDetails) {
    User user = customUserDetails.getUser();
    Income income = createIncomeRequest.toEntity(user);
    incomeRepository.save(income);
  }

  @Transactional(readOnly = true)
  public List<Income> getIncomesByMonth(CustomUserDetails customUserDetails, int year, int month) {
    Long userId = customUserDetails.getUserId();

    LocalDate startOfMonth = LocalDate.of(year, month, 1);
    LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

    return incomeRepository.findAllByUserIdAndDateBetweenOrderByDateAscIdAsc(userId, startOfMonth,
        endOfMonth);
  }

  @Transactional(readOnly = true)
  public List<Income> getIncomeByDay(CustomUserDetails customUserDetails, LocalDate date) {
    Long id = customUserDetails.getUserId();

    return incomeRepository.findAllByUserIdAndDateBetweenOrderByDateAscIdAsc(id, date, date);
  }

  @Transactional(readOnly = true)

  public Income getIncomeById(CustomUserDetails customUserDetails, Long incomeId) {
    User user = customUserDetails.getUser();
    return incomeRepository.findByIdAndUser(incomeId, user).orElseThrow(()->new BaseException(
        BaseResponseStatus.NO_INCOME));
  }

  @Transactional
  public void updateIncome(Long incomeId, UpdateIncomeRequest updateIncomeRequest, CustomUserDetails customUserDetails) {
    User user = customUserDetails.getUser();

    Income findIncome = incomeRepository.findByIdAndUser(incomeId,user).orElseThrow(()->new BaseException(BaseResponseStatus.NO_INCOME));

    findIncome.update(updateIncomeRequest.toEntity());
  }

  public void deleteIncome(CustomUserDetails customUserDetails, Long incomeId) {
    User user = customUserDetails.getUser();

    Income findIncome = incomeRepository.findByIdAndUser(incomeId, user).orElseThrow(()->new BaseException(BaseResponseStatus.NO_INCOME));

    incomeRepository.delete(findIncome);
  }
}
