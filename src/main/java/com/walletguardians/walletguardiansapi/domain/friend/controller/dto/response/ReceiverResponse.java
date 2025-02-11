package com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendStatus;
import com.walletguardians.walletguardiansapi.domain.friend.entity.status.FriendStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverResponse {

  private Long friendStatusId;
  private String receiverEmail;
  private String senderEmail;
  private String senderUsername;
  private FriendStatusEnum friendStatus;

  public ReceiverResponse(FriendStatus friendStatus) {
    this.friendStatusId = friendStatus.getId();
    this.receiverEmail = friendStatus.getReceiver().getEmail();
    this.senderEmail = friendStatus.getSender().getEmail();
    this.senderUsername = friendStatus.getSender().getUsername();
    this.friendStatus = friendStatus.getFriendStatus();
  }

  public static ReceiverResponse fromEntity(FriendStatus friendStatus) {
    return new ReceiverResponse(
        friendStatus.getId(),
        friendStatus.getSender().getEmail(),
        friendStatus.getReceiver().getEmail(),
        friendStatus.getReceiver().getUsername(),
        friendStatus.getFriendStatus()
    );
  }

}
