package com.walletguardians.walletguardiansapi.domain.budget.controller.dto;

import com.walletguardians.walletguardiansapi.domain.budget.entity.Budget;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetRequest {

  private int amount;

  private LocalDate date;

  private User user;

  public Budget toEntity(){
    return Budget.builder()
        .amount(amount)
        .date(LocalDate.now())
        .user(user)
        .build();
  }

}
