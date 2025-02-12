package com.walletguardians.walletguardiansapi.domain.expenses.repository;

import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

  Optional<Expense> findById(Long id);

  Optional<Expense> findByIdAndUserId(Long id, Long userId);

  List<Expense> findAllByUserIdAndDateBetweenOrderByDateAscIdAsc(Long userId, LocalDate startTime,
      LocalDate endDate);

  @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.id = :userId AND DATE_FORMAT(e.date, '%Y-%m') = DATE_FORMAT(:month, '%Y-%m')")
  int getTotalSpentByUserIdAndMonth(@Param("userId") Long userId, @Param("month") LocalDate month);

}
