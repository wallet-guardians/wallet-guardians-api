package com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendState;
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

  public static ReceiverResponse fromEntity(FriendState friendState) {
    return ReceiverResponse.builder()
        .friendStatusId(friendState.getId())
        .receiverEmail(friendState.getReceiver().getEmail())
        .senderEmail(friendState.getSender().getEmail())
        .senderUsername(friendState.getSender().getUsername())
        .friendStatus(friendState.getFriendStatus())
        .build();
  }

}
