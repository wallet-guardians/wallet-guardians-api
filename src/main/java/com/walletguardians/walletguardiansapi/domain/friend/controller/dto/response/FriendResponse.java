package com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response;

import com.walletguardians.walletguardiansapi.domain.friend.entity.Friend;
import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendStatusEnum;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendResponse {

    private Long id;
    private String senderEmail;
    private String receiverEmail;
    private String receiverUsername;
    private FriendStatusEnum friendStatus;

    public FriendResponse(Friend friend) {
        this.id = friend.getId();
        this.senderEmail = friend.getSender().getEmail();
        this.receiverEmail = friend.getReceiver().getEmail();
        this.receiverUsername = friend.getReceiver().getUsername();
        this.friendStatus = friend.getFriendStatus();
    }

    public static FriendResponse fromEntity(Friend friend) {
        return new FriendResponse(
            friend.getId(),
            friend.getSender().getEmail(),
            friend.getReceiver().getEmail(),
            friend.getReceiver().getUsername(),
            friend.getFriendStatus()
        );
    }
}
