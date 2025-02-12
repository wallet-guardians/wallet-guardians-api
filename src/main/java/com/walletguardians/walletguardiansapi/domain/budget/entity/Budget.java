package com.walletguardians.walletguardiansapi.domain.budget.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Budget {

  @Id
  @JsonIgnore
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "budget_id")
  private Long id;

  @Column(nullable = false)
  private int amount;

  private LocalDate date;

  @JsonIgnore
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  public void updateBudget(int newAmount){
    this.amount = newAmount;
  }

}
