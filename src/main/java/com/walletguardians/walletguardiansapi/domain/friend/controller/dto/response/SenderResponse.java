package com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendStatus;
import com.walletguardians.walletguardiansapi.domain.friend.entity.status.FriendStatusEnum;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SenderResponse {

  private Long friendStatusId;
  private String senderEmail;
  private String receiverEmail;
  private String receiverUsername;
  private FriendStatusEnum friendStatus;

  public SenderResponse(FriendStatus friendStatus) {
    this.friendStatusId = friendStatus.getId();
    this.senderEmail = friendStatus.getSender().getEmail();
    this.receiverEmail = friendStatus.getReceiver().getEmail();
    this.receiverUsername = friendStatus.getReceiver().getUsername();
    this.friendStatus = friendStatus.getFriendStatus();
  }

  public static SenderResponse fromEntity(FriendStatus friendStatus) {
    return new SenderResponse(
        friendStatus.getId(),
        friendStatus.getSender().getEmail(),
        friendStatus.getReceiver().getEmail(),
        friendStatus.getReceiver().getUsername(),
        friendStatus.getFriendStatus()
    );
  }
}
