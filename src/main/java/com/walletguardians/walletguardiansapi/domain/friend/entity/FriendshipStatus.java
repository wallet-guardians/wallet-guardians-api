package com.walletguardians.walletguardiansapi.domain.friend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "FRIENDSHIP_STATUS")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "friendship_status", nullable = false)
    private FriendshipStatusEnum friendshipStatus;

    @Builder
    public FriendshipStatus(User sender, User receiver, FriendshipStatusEnum friendshipStatus) {
        this.sender = sender;
        this.receiver = receiver;
        this.friendshipStatus = friendshipStatus;
    }

    public void updateFriendshipStatus(FriendshipStatusEnum status) {
        this.friendshipStatus = status;
    }
}
