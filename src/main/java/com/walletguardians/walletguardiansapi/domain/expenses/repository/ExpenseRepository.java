package com.walletguardians.walletguardiansapi.domain.expenses.repository;

import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findById(Long id);

    Optional<Expense> findByIdAndUserId(Long id, Long userId);

    List<Expense> findAllByUserIdAndDateBetweenOrderByDateAscIdAsc(Long userId, LocalDate startTime, LocalDate endDate);
}
