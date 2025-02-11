package com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response;

import com.walletguardians.walletguardiansapi.domain.friend.entity.Friend;
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
public class FriendResponse {

  private Long friendListId;
  private String friendEmail;
  private String friendName;

  public static FriendResponse fromEntity(Friend friend) {
    return FriendResponse.builder()
        .friendListId(friend.getId())
        .friendEmail(friend.getFriendEntity().getEmail())
        .friendName(friend.getFriendEntity().getUsername())
        .build();
  }
}
