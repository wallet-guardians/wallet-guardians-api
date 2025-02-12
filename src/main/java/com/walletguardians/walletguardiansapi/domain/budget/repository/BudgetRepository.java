package com.walletguardians.walletguardiansapi.domain.budget.repository;

import com.walletguardians.walletguardiansapi.domain.budget.entity.Budget;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
  Optional<Budget> findByUser(User user);

  @Query("SELECT COALESCE(b.amount, 0) FROM Budget b WHERE b.user.id = :userId AND FUNCTION('DATE_FORMAT', b.date, '%Y-%m') = FUNCTION('DATE_FORMAT', :month, '%Y-%m')")
  Integer getBudgetByUserIdAndMonth(@Param("userId") Long userId, @Param("month") LocalDate month);

}
