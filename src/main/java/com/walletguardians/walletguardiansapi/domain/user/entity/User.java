package com.walletguardians.walletguardiansapi.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walletguardians.walletguardiansapi.domain.budget.entity.Budget;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.domain.friend.entity.Friend;
import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendState;
import com.walletguardians.walletguardiansapi.domain.income.entity.Income;
import com.walletguardians.walletguardiansapi.domain.user.entity.auth.Role;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false, unique = true, length = 30)
  private String email;

  private String password;

  @Default
  private String budgetTitle = null;

  @Default
  private String expenseTitle = null;

  @Default
  @Column(nullable = false, name = "defense")
  private int defense = 60;

  @Enumerated(EnumType.STRING)
  private Role role;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Expense> expenses;

  @JsonIgnore
  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FriendState> receivedList;

  @JsonIgnore
  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FriendState> sentList;

  @JsonIgnore
  @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Friend> friends;

  @JsonIgnore
  @OneToMany(mappedBy = "friendEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Friend> friendOf;

  @JsonIgnore
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Budget budget;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Income> incomes;

  @Column(name = "profile_image_url")
  private String profileImagePath;


  public void encodePassword(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(password);
  }

  public boolean isPasswordValid(PasswordEncoder passwordEncoder, String password) {
    return passwordEncoder.matches(password, this.password);
  }

  public void updatePassword(PasswordEncoder passwordEncoder, String newPassword) {
    this.password = passwordEncoder.encode(newPassword);
  }

  public void updateBudgetTitle(String newTitle) {
    this.budgetTitle = newTitle;
  }

  public void updateExpenseTitle(String newTitle) {
    this.expenseTitle = newTitle;
  }

  public void decreaseDefense(int defense) {
    this.defense -= defense;
  }

  public void increaseDefense(int defense) {
    this.defense += defense;
  }

  public void updateProfileImage(String imageUrl) {
    this.profileImagePath = imageUrl;
  }
}
