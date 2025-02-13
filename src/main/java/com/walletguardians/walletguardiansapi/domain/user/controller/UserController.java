package com.walletguardians.walletguardiansapi.domain.user.controller;

import com.walletguardians.walletguardiansapi.domain.user.controller.dto.request.UpdateUserRequest;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.service.UserService;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.auth.jwt.service.JwtService;
import com.walletguardians.walletguardiansapi.global.response.BaseResponse;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

  private final UserService userService;
  private final BaseResponseService baseResponseService;
  private final JwtService jwtService;

  @GetMapping("/info")
  public ResponseEntity<BaseResponse<User>> getUserInfo(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    User user = userService.findUserByUserId(customUserDetails.getUserId());
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(user));
  }

  @PatchMapping("/password")
  public ResponseEntity<BaseResponse<User>> updateUserInfo(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody UpdateUserRequest updateUserRequest) {
    User updatedUser = userService.updatePassword(customUserDetails.getUserId(), updateUserRequest);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(updatedUser));
  }

  @PostMapping("/logout")
  public ResponseEntity<BaseResponse<Void>> logout(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      HttpServletRequest request) {
    String accessToken = jwtService.extractAccessToken(request)
        .filter(jwtService::validateToken)
        .orElse(null);
    userService.logout(accessToken, customUserDetails.getUsername());
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse());
  }

  @DeleteMapping("/delete")
  public ResponseEntity<BaseResponse<Void>> deleteUser(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    userService.deleteById(customUserDetails.getUserId());
    jwtService.deleteRefreshTokenByEmail(customUserDetails.getUsername());
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse());
  }

  @PutMapping("/profile")
  public ResponseEntity<BaseResponse<String>> uploadProfilePicture(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestParam("file") MultipartFile file) {

    String imageUrl = userService.uploadProfilePicture(customUserDetails.getUserId(), file, customUserDetails);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(imageUrl));
  }


  @GetMapping("/me")
  public ResponseEntity<BaseResponse<User>> getUserProfile(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User user = userService.findUserByUserId(customUserDetails.getUserId());
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(user));
  }
}
