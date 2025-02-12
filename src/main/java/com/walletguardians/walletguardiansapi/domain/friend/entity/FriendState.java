package com.walletguardians.walletguardiansapi.domain.friend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walletguardians.walletguardiansapi.domain.friend.entity.status.FriendStatusEnum;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;

@Entity
@Getter
@Table(name = "friend_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendState implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "friend_status_id")
  private Long id;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id", nullable = false)
  private User sender;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id", nullable = false)
  private User receiver;

  @Enumerated(EnumType.STRING)
  @Column(name = "friend_status", nullable = false)
  private FriendStatusEnum friendStatus;

  @Builder
  public FriendState(User sender, User receiver, FriendStatusEnum friendStatus) {
    this.sender = sender;
    this.receiver = receiver;
    this.friendStatus = friendStatus;
  }

  public void updateFriendStatus(FriendStatusEnum status) {
    this.friendStatus = status;
  }

}
