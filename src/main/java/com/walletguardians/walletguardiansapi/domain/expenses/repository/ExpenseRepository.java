package com.walletguardians.walletguardiansapi.domain.expenses.repository;

import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findById(Long id);
}
