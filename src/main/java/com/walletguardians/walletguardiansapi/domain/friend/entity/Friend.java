package com.walletguardians.walletguardiansapi.domain.friend.entity;

import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "friends")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friend {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "friend_list_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User userEntity;

  @ManyToOne
  @JoinColumn(name = "friend_id", nullable = false)
  private User friendEntity;

}
