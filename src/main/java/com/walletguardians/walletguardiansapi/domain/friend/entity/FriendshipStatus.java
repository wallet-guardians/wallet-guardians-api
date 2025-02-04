package com.walletguardians.walletguardiansapi.domain.friend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "friendship_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendshipStatus {

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

    @Enumerated(EnumType.STRING) //Enum을 String으로 저장하여 JPQL 호환
    @Column(name = "friendship_status", nullable = false)
    private FriendshipStatusEnum friendshipStatus;

    @Builder
    public FriendshipStatus(User sender, User receiver, FriendshipStatusEnum friendshipStatus) {
        this.sender = sender;
        this.receiver = receiver;
        this.friendshipStatus = friendshipStatus;
    }

    //친구 상태 업데이트 메서드 (ACCEPTED, PENDING 등)
    public void updateFriendshipStatus(FriendshipStatusEnum status) {
        this.friendshipStatus = status;
    }

    //친구 요청이 PENDING 상태인지 확인
    public boolean isPending() {
        return this.friendshipStatus == FriendshipStatusEnum.PENDING;
    }

    //친구 상태가 ACCEPTED인지 확인
    public boolean isAccepted() {
        return this.friendshipStatus == FriendshipStatusEnum.ACCEPTED;
    }
}
