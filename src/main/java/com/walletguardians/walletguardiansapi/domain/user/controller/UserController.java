package com.walletguardians.walletguardiansapi.domain.user.controller;

import com.walletguardians.walletguardiansapi.domain.user.dto.request.UserUpdateRequest;
import com.walletguardians.walletguardiansapi.domain.user.dto.response.UserProfileResponse;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.service.UserService;
import com.walletguardians.walletguardiansapi.global.auth.jwt.service.JwtService;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponse;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseService;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

  private final UserService userService;
  private final BaseResponseService baseResponseService;
  private final JwtService jwtService;

  @GetMapping("/{userId}")
  public ResponseEntity<BaseResponse<User>> getUserInfo(@PathVariable("userId") Long userId) {
    User user = userService.findByUserId(userId);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(user));
  }

  @GetMapping("/me")
  public ResponseEntity<BaseResponse<UserProfileResponse>> getUserProfile(HttpServletRequest request) {
    String token = jwtService.extractAccessToken(request)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.UNAUTHORIZED));
    String email = jwtService.getUserPk(token);
    User user = userService.findUserByEmail(email);

    return ResponseEntity.ok(baseResponseService.getSuccessResponse(new UserProfileResponse(user)));
  }

  @PatchMapping("/me")
  public ResponseEntity<BaseResponse<User>> updateUserProfile(
      HttpServletRequest request,
      @RequestBody UserUpdateRequest updateRequest) {

    String token = jwtService.extractAccessToken(request)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.UNAUTHORIZED));
    String email = jwtService.getUserPk(token);
    User updatedUser = userService.updateUserProfile(email, updateRequest);

    return ResponseEntity.ok(baseResponseService.getSuccessResponse(updatedUser));
  }
}
