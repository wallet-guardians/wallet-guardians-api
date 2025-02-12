package com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendState;
import com.walletguardians.walletguardiansapi.domain.friend.entity.status.FriendStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SenderResponse {

  private Long friendStatusId;
  private String senderEmail;
  private String receiverEmail;
  private String receiverUsername;
  private FriendStatusEnum friendStatus;

  public static SenderResponse fromEntity(FriendState friendState) {
    return SenderResponse.builder()
        .friendStatusId(friendState.getId())
        .senderEmail(friendState.getSender().getEmail())
        .receiverEmail(friendState.getReceiver().getEmail())
        .receiverUsername(friendState.getReceiver().getUsername())
        .friendStatus(friendState.getFriendStatus())
        .build();
  }
}
