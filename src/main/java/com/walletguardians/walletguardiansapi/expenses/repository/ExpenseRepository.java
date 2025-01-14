package com.walletguardians.walletguardiansapi.expenses.repository;

import com.walletguardians.walletguardiansapi.expenses.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findById(Long id);
}
