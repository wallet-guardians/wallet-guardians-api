package com.walletguardians.walletguardiansapi.domain.income.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Income {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "income_id")
  private Long id;

  @Column(nullable = false)
  private int amount;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  private String category;

  private String description;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public void update(Income updateIncome) {
    this.category = updateIncome.category;
    this.amount = updateIncome.amount;
    this.description = updateIncome.description;
  }

}
