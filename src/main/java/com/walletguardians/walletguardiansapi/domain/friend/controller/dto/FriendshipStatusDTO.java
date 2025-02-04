package com.walletguardians.walletguardiansapi.domain.friend.controller.dto;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatus;
import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatusEnum;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FriendshipStatusDTO {

    private Long id;
    private String senderEmail;
    private String receiverEmail;
    private FriendshipStatusEnum friendshipStatus;

    public FriendshipStatusDTO(FriendshipStatus friendshipStatus) {
        this.id = friendshipStatus.getId();
        this.senderEmail = friendshipStatus.getSender().getEmail();
        this.receiverEmail = friendshipStatus.getReceiver().getEmail();
        this.friendshipStatus = friendshipStatus.getFriendshipStatus();
    }

    public static FriendshipStatusDTO fromEntity(FriendshipStatus friendshipStatus) {
        return new FriendshipStatusDTO(
            friendshipStatus.getId(),
            friendshipStatus.getSender().getEmail(),
            friendshipStatus.getReceiver().getEmail(),
            friendshipStatus.getFriendshipStatus()
        );
    }
}
