package com.walletguardians.walletguardiansapi.domain.friend.controller.dto;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FriendshipStatusDTO {
    private Long id;
    private String senderEmail;
    private String receiverEmail;
    private boolean friendshipStatus;

    public FriendshipStatusDTO(FriendshipStatus friendshipStatus) {
        this.id = friendshipStatus.getId();
        this.senderEmail = friendshipStatus.getSender().getEmail();
        this.receiverEmail = friendshipStatus.getReceiver().getEmail();
        this.friendshipStatus = friendshipStatus.isFriendshipStatus();
    }

    public static FriendshipStatusDTO fromEntity(FriendshipStatus friendshipStatus) {
        return new FriendshipStatusDTO(
            friendshipStatus.getId(),
            friendshipStatus.getSender().getEmail(),
            friendshipStatus.getReceiver().getEmail(),
            friendshipStatus.isFriendshipStatus()
        );
    }
}
