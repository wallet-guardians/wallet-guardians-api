package com.walletguardians.walletguardiansapi.domain.friend.controller;

import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.FriendshipStatusDTO;
import com.walletguardians.walletguardiansapi.domain.friend.service.FriendshipService;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.service.UserService;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.auth.jwt.service.JwtService;
import com.walletguardians.walletguardiansapi.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendshipController {

  private final FriendshipService friendshipService;
  private final UserService userService;
  private final JwtService jwtService;

  private String extractEmailFromToken(String token) {
    if (token == null || token.isEmpty()) {
      throw new IllegalArgumentException("토큰이 제공되지 않았습니다.");
    }
    return jwtService.getUserPk(token.replaceFirst("(?i)bearer ", "").trim());
  }

  @PostMapping("/requests")
  public ResponseEntity<BaseResponse<FriendshipStatusDTO>> sendFriendRequest(
      @RequestHeader("ACCESS-AUTH-KEY") String accessToken,
      @RequestBody Map<String, String> requestBody) {

    String senderEmail = extractEmailFromToken(accessToken);
    String receiverEmail = requestBody.get("receiverEmail");

    if (receiverEmail == null || receiverEmail.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new BaseResponse<>(false, "receiverEmail이 필요합니다.", 400, null));
    }

    User sender = userService.findUserByEmail(senderEmail);
    User receiver = userService.findUserByEmail(receiverEmail);

    FriendshipStatusDTO friendRequest = friendshipService.sendFriendRequest(sender, receiver);
    return ResponseEntity.ok(new BaseResponse<>(true, "친구 요청이 전송되었습니다.", 200, friendRequest));
  }

  @PutMapping("/accept")
  public ResponseEntity<BaseResponse<String>> acceptFriendRequest(
      @RequestHeader("ACCESS-AUTH-KEY") String accessToken,
      @RequestBody Map<String, String> requestBody) {

    String receiverEmail = extractEmailFromToken(accessToken);
    String senderEmail = requestBody.get("senderEmail");

    if (senderEmail == null || senderEmail.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new BaseResponse<>(false, "senderEmail이 필요합니다.", 400, null));
    }

    boolean success = friendshipService.acceptFriendRequest(receiverEmail, senderEmail);
    if (success) {
      return ResponseEntity.ok(new BaseResponse<>(true, "친구 요청 수락 완료", 200, null));
    } else {
      return ResponseEntity.badRequest()
          .body(new BaseResponse<>(false, "친구 요청 수락 실패 (존재하지 않는 요청)", 400, null));
    }
  }

  @GetMapping("/requests")
  public ResponseEntity<List<FriendshipStatusDTO>> getFriendRequests(
      @RequestParam(required = false, defaultValue = "accepted") String status,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    String userEmail = userDetails.getUsername();
    List<FriendshipStatusDTO> requests;

    switch (status.toLowerCase()) {
      case "accepted":
        requests = friendshipService.getAcceptedFriends(userEmail);
        break;
      case "pending":
        requests = friendshipService.getPendingRequests(userEmail);
        break;
      case "requested":
        requests = friendshipService.getReceivedRequests(userEmail);
        break;
      default:
        throw new IllegalArgumentException(
            "유효하지 않은 상태 값입니다. (accepted, pending, requested 중 하나를 사용하세요)");
    }

    return ResponseEntity.ok(requests);
  }

  @DeleteMapping("/reject")
  public ResponseEntity<BaseResponse<String>> rejectFriendRequest(
      @RequestHeader("ACCESS-AUTH-KEY") String accessToken,
      @RequestBody Map<String, String> requestBody) {

    String receiverEmail = extractEmailFromToken(accessToken);
    String senderEmail = requestBody.get("senderEmail");

    if (senderEmail == null || senderEmail.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new BaseResponse<>(false, "senderEmail이 필요합니다.", 400, null));
    }

    boolean success = friendshipService.rejectFriendRequest(receiverEmail, senderEmail);
    if (success) {
      return ResponseEntity.ok(new BaseResponse<>(true, "친구 요청을 거절했습니다.", 200, null));
    } else {
      return ResponseEntity.badRequest()
          .body(new BaseResponse<>(false, "거절할 친구 요청이 없습니다.", 400, null));
    }
  }

  @DeleteMapping("/delete")
  public ResponseEntity<BaseResponse<String>> deleteFriendship(
      @RequestHeader("ACCESS-AUTH-KEY") String accessToken,
      @RequestBody Map<String, String> requestBody) {

    String userEmail = extractEmailFromToken(accessToken);
    String targetEmail = requestBody.get("deleteEmail");

    if (targetEmail == null || targetEmail.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new BaseResponse<>(false, "삭제할 이메일이 필요합니다.", 400, null));
    }

    boolean success = friendshipService.deleteFriendship(userEmail, targetEmail);
    if (success) {
      return ResponseEntity.ok(new BaseResponse<>(true, "친구 관계를 삭제했습니다.", 200, null));
    } else {
      return ResponseEntity.badRequest()
          .body(new BaseResponse<>(false, "삭제할 친구 관계가 없습니다.", 400, null));
    }
  }

  @DeleteMapping("/cancel-request")
  public ResponseEntity<BaseResponse<String>> cancelFriendRequest(
      @RequestHeader("ACCESS-AUTH-KEY") String accessToken,
      @RequestBody Map<String, String> requestBody) {

    String senderEmail = extractEmailFromToken(accessToken);
    String receiverEmail = requestBody.get("receiverEmail");

    if (receiverEmail == null || receiverEmail.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new BaseResponse<>(false, "receiverEmail이 필요합니다.", 400, null));
    }

    boolean success = friendshipService.cancelFriendRequest(senderEmail, receiverEmail);
    if (success) {
      return ResponseEntity.ok(new BaseResponse<>(true, "친구 요청을 취소했습니다.", 200, null));
    } else {
      return ResponseEntity.badRequest()
          .body(new BaseResponse<>(false, "취소할 친구 요청이 없습니다.", 400, null));
    }
  }
}