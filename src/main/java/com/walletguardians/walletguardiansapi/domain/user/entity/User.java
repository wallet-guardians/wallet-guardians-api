package com.walletguardians.walletguardiansapi.domain.user.entity;

import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatus;
import com.walletguardians.walletguardiansapi.domain.user.entity.auth.Role;
import jakarta.persistence.*;
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
@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false, unique = true, length = 30)
  private String email;

  @Column(nullable = false)
  private String password;

  @Default
  private String title = "";

  @Column(nullable = false, name = "defense_rate")
  private float defenseRate = 0;

  @Column(nullable = false, name = "user_deleted")
  private boolean userDeleted = false;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Expense> expenses;

  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FriendshipStatus> followerList;

  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FriendshipStatus> followingList;

  public void encodePassword(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(password);
  }

  public boolean isPasswordValid(PasswordEncoder passwordEncoder, String password) {
    return passwordEncoder.matches(password, this.password);
  }
}
