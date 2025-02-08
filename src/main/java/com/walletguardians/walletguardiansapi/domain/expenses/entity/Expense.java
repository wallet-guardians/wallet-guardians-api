package com.walletguardians.walletguardiansapi.domain.expenses.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "expense_id")
  private Long id;

  @Column(nullable = false)
  private int amount;

  private String description;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  private String category;

  @Column(nullable = false, name = "store_name")
  private String storeName;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public void update(Expense updateExpense) {
    this.category = updateExpense.category;
    this.amount = updateExpense.amount;
    this.storeName = updateExpense.storeName;
    this.description = updateExpense.description;
  }
}
