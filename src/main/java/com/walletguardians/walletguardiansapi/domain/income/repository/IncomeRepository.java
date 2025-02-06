package com.walletguardians.walletguardiansapi.domain.income.repository;

import com.walletguardians.walletguardiansapi.domain.income.entity.Income;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {

  List<Income> findAllByUserIdAndDateBetweenOrderByDateAscIdAsc(Long id, LocalDate startOfTime, LocalDate endOfTime);

  Optional<Income> findByIdAndUser(Long id, User user);
}
