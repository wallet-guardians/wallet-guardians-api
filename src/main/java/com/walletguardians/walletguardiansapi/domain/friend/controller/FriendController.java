package com.walletguardians.walletguardiansapi.domain.friend.controller;

import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.request.FriendSenderRequest;
import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.request.FriendReceiverRequest;
import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response.FriendResponse;
import com.walletguardians.walletguardiansapi.domain.friend.service.FriendService;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.response.BaseResponse;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

  private final FriendService friendService;
  private final BaseResponseService baseResponseService;

  @PostMapping("/requests")
  public ResponseEntity<BaseResponse<FriendResponse>> sendFriendRequest(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody FriendSenderRequest friendSenderRequest) {
    FriendResponse friendStatusResponse = friendService.sendFriendRequest(
        customUserDetails, friendSenderRequest);
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse(friendStatusResponse));
  }

  @PutMapping("/accept")
  public ResponseEntity<BaseResponse<Void>> acceptFriendRequest(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody FriendReceiverRequest friendReceiverRequest) {
    friendService.acceptFriendRequest(customUserDetails, friendReceiverRequest);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse());
  }

  @GetMapping("/requests")
  public ResponseEntity<BaseResponse<List<FriendResponse>>> getFriendRequests(
      @RequestParam(required = false, defaultValue = "accepted") String status,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    List<FriendResponse> responses = friendService.getAcceptedOrPendingOrReceivedRequest(
        customUserDetails, status);
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse(responses));
  }


  @DeleteMapping("/reject")
  public ResponseEntity<BaseResponse<Void>> rejectFriendRequest(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody FriendReceiverRequest friendReceiverRequest) {
    friendService.rejectFriendRequest(customUserDetails, friendReceiverRequest);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse());
  }

  @DeleteMapping("/delete")
  public ResponseEntity<BaseResponse<Void>> deleteFriend(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody FriendReceiverRequest friendReceiverRequest) {
    friendService.deleteFriend(customUserDetails, friendReceiverRequest);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse());
  }

  @DeleteMapping("/cancel-request")
  public ResponseEntity<BaseResponse<Void>> cancelFriendRequest(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody FriendSenderRequest friendSenderRequest) {
    friendService.cancelFriendRequest(customUserDetails, friendSenderRequest);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse());
  }
}