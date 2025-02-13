package com.walletguardians.walletguardiansapi.domain.friend.controller;

import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response.FriendResponse;
import com.walletguardians.walletguardiansapi.domain.friend.service.FriendService;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.response.BaseResponse;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {

  private final FriendService friendService;
  private final BaseResponseService baseResponseService;

  @GetMapping()
  public ResponseEntity<BaseResponse<List<FriendResponse>>> getAllFriends(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    List<FriendResponse> friends = friendService.getAllFriends(customUserDetails);
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse(friends));
  }

  @DeleteMapping("/{friendListId}")
  public ResponseEntity<BaseResponse<Void>> deleteFriend(
      @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long friendListId) {
    friendService.deleteFriend(customUserDetails, friendListId);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse());
  }

}
