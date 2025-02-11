package com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response;

import com.walletguardians.walletguardiansapi.domain.friend.entity.Friend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendResponse {

  private Long friendListId;
  private String email;
  private String friendName;

  public FriendResponse(Friend friend) {
    this.friendListId = friend.getId();
    this.email = friend.getFriend().getEmail();
    this.friendName = friend.getFriend().getUsername();
  }

  public static FriendResponse fromEntity(Friend friend) {
    return new FriendResponse(
        friend.getId(),
        friend.getFriend().getEmail(),
        friend.getFriend().getUsername()
    );
  }
}
