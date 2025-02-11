package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.repository.ExpenseRepository;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
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
    public void createExpense(User user, CreateExpenseRequest createExpenseRequest) {
        Expense expense = createExpenseRequest.toEntity(user);
        expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getExpensesByMonth(Long userId, int year,
            int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        return expenseRepository.findAllByUserIdAndDateBetweenOrderByDateAscIdAsc(userId,
                startOfMonth, endOfMonth);
    }

    @Override
    public List<Expense> getExpensesByDay(Long userId, LocalDate date) {
        return expenseRepository.findAllByUserIdAndDateBetweenOrderByDateAscIdAsc(userId, date,
                date);
    }

    @Override
    public Expense getExpenseById(Long userId, Long expenseId) {
        return expenseRepository.findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXPENSES));
    }

    @Override
    public Expense getExpenseByIdAndUserId(Long userId, Long expenseId) {
        return expenseRepository.findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXPENSES));
    }

    @Override
    @Transactional
    public void updateExpense(Expense findExpense, UpdateExpenseRequest updateExpenseRequest) {
        findExpense.update(updateExpenseRequest.toEntity());
    }

    @Override
    @Transactional
    public void deleteExpense(Expense findExpense) {
        expenseRepository.delete(findExpense);
    }
}
