package com.walletguardians.walletguardiansapi.domain.friend.entity;

import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "FRIENDSHIP_STATUS")
public class FriendshipStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private Long id;

    // 친구 요청을 보낸 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    // 친구 요청을 받은 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    // 친구 상태 (true: 친구, false: 요청 중)
    @Column(name = "friendship_status", nullable = false)
    private boolean friendshipStatus;

    @Builder
    public FriendshipStatus(User sender, User receiver, boolean friendshipStatus) {
        this.sender = sender;
        this.receiver = receiver;
        this.friendshipStatus = friendshipStatus;
    }
}
