package com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendStatus;
import com.walletguardians.walletguardiansapi.domain.friend.entity.status.FriendStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverResponse {

  private Long friendStatusId;
  private String receiverEmail;
  private String senderEmail;
  private String senderUsername;
  private FriendStatusEnum friendStatus;

  public static ReceiverResponse fromEntity(FriendStatus friendStatus) {
    return ReceiverResponse.builder()
        .friendStatusId(friendStatus.getId())
        .receiverEmail(friendStatus.getReceiver().getEmail())
        .senderEmail(friendStatus.getSender().getEmail())
        .senderUsername(friendStatus.getSender().getUsername())
        .friendStatus(friendStatus.getFriendStatus())
        .build();
  }

}
