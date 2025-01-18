package com.walletguardians.global.auth.controller;

import com.walletguardians.domain.user.dto.request.UserLoginRegister;
import com.walletguardians.domain.user.dto.request.UserRegisterRequest;
import com.walletguardians.global.auth.jwt.dto.TokenDto;
import com.walletguardians.global.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auths")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody UserRegisterRequest userRegisterRequest) {
    authService.registerUser(userRegisterRequest);
    return ResponseEntity.ok().body("성공적으로 회원등록이 완료되었습니다.");
  }

  @PostMapping("/login")
  public ResponseEntity<TokenDto> login(@RequestBody UserLoginRegister userLoginRegister) {
    return ResponseEntity.ok().body(authService.login(userLoginRegister));
  }

}