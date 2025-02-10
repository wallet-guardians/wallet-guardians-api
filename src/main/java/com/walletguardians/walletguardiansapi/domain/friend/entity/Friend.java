package com.walletguardians.walletguardiansapi.domain.friend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walletguardians.walletguardiansapi.domain.friend.entity.status.FriendStatusEnum;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "friend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
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
    public Friend(User sender, User receiver, FriendStatusEnum friendStatus) {
        this.sender = sender;
        this.receiver = receiver;
        this.friendStatus = friendStatus;
    }

    public void updateFriendStatus(FriendStatusEnum status) {
        this.friendStatus = status;
    }

    //친구 요청이 PENDING 상태인지 확인
    public boolean isPending() {
        return this.friendStatus == FriendStatusEnum.PENDING;
    }

    //친구 상태가 ACCEPTED인지 확인
    public boolean isAccepted() {
        return this.friendStatus == FriendStatusEnum.ACCEPTED;
    }
}
