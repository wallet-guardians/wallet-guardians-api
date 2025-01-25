package com.walletguardians.walletguardiansapi.global.auth.controller;

import com.walletguardians.walletguardiansapi.domain.user.dto.request.UserLoginRegister;
import com.walletguardians.walletguardiansapi.domain.user.dto.request.UserRegisterRequest;
import com.walletguardians.walletguardiansapi.global.auth.jwt.dto.TokenDto;
import com.walletguardians.walletguardiansapi.global.auth.service.AuthService;
import com.walletguardians.walletguardiansapi.global.response.BaseResponse;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final BaseResponseService baseResponseService;

  @PostMapping("/sign-up")
  public ResponseEntity<BaseResponse<Void>> register(@RequestBody UserRegisterRequest userRegisterRequest) {
    authService.registerUser(userRegisterRequest);
    return ResponseEntity.ok().body(baseResponseService.getSuccessResponse());
  }

  @PostMapping("/login")
  public ResponseEntity<BaseResponse<TokenDto>> login(@RequestBody UserLoginRegister userLoginRegister) {
    TokenDto tokenDto = authService.login(userLoginRegister);
    return ResponseEntity.ok(baseResponseService.getSuccessResponse(tokenDto));
  }
}
