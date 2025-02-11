package com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendStatus;
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

  public static SenderResponse fromEntity(FriendStatus friendStatus) {
    return SenderResponse.builder()
        .friendStatusId(friendStatus.getId())
        .senderEmail(friendStatus.getSender().getEmail())
        .receiverEmail(friendStatus.getReceiver().getEmail())
        .receiverUsername(friendStatus.getReceiver().getUsername())
        .friendStatus(friendStatus.getFriendStatus())
        .build();
  }
}
