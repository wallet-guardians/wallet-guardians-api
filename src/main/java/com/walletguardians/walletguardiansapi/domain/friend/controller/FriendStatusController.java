package com.walletguardians.walletguardiansapi.domain.friend.controller;

import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.request.SenderRequest;
import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.request.ReceiverRequest;
import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response.SenderResponse;
import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response.ReceiverResponse;
import com.walletguardians.walletguardiansapi.domain.friend.service.FriendStatusService;
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
public class FriendStatusController {

  private final FriendStatusService friendStatusService;
  private final BaseResponseService baseResponseService;

  @PostMapping("/requests")
  public ResponseEntity<BaseResponse<SenderResponse>> sendFriendRequest(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody SenderRequest senderRequest) {
    SenderResponse senderResponse = friendStatusService.sendFriendRequest(
        customUserDetails, senderRequest);
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse(
        senderResponse));
  }

  @PatchMapping("/requests/{friendStatusId}/accept")
  public ResponseEntity<BaseResponse<Void>> acceptFriendRequest(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable Long friendStatusId) {
    friendStatusService.acceptFriendRequest(customUserDetails, friendStatusId);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse());
  }

  @GetMapping("/requests/sent")
  public ResponseEntity<BaseResponse<List<SenderResponse>>> getSentRequests(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    List<SenderResponse> responses = friendStatusService.getSentRequests(customUserDetails);
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse(responses));
  }

  @GetMapping("/requests/received")
  public ResponseEntity<BaseResponse<List<ReceiverResponse>>> getRecivedRequests(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    List<ReceiverResponse> responses = friendStatusService.getReceivedRequests(customUserDetails);
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse(responses));
  }

  @DeleteMapping("/requests/{friendStatusId}/reject")
  public ResponseEntity<BaseResponse<Void>> rejectFriendRequest(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable Long friendStatusId) {
    friendStatusService.rejectFriendRequest(customUserDetails, friendStatusId);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse());
  }

  @DeleteMapping("/requests/{friendStatusId}/cancel")
  public ResponseEntity<BaseResponse<Void>> cancelFriendRequest(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable Long friendStatusId) {
    friendStatusService.cancelFriendRequest(customUserDetails, friendStatusId);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse());
  }
}