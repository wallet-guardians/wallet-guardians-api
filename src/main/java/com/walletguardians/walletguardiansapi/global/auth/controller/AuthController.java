package com.walletguardians.walletguardiansapi.global.auth.controller;

import com.walletguardians.walletguardiansapi.domain.user.dto.request.UserLoginRegister;
import com.walletguardians.walletguardiansapi.domain.user.dto.request.UserRegisterRequest;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.auth.jwt.dto.TokenDto;
import com.walletguardians.walletguardiansapi.global.auth.jwt.service.JwtService;
import com.walletguardians.walletguardiansapi.global.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final JwtService jwtService;
  private final AuthService authService;

  @PostMapping("/sign-up")
  public ResponseEntity<String> signUp(@RequestBody UserRegisterRequest userRegisterRequest) {
    authService.signUpUser(userRegisterRequest);
    return ResponseEntity.ok().body("성공적으로 회원등록이 완료되었습니다.");
  }

  @PostMapping("/login")
  public ResponseEntity<TokenDto> login(@RequestBody UserLoginRegister userLoginRegister) {
    return ResponseEntity.ok().body(authService.login(userLoginRegister));
  }

  @DeleteMapping("/log-out")
  public ResponseEntity<String> logout(@AuthenticationPrincipal CustomUserDetails customUserDetails,
      HttpServletRequest request) {
    String accessToken = jwtService.extractAccessToken(request)
        .filter(jwtService::validateToken)
        .orElse(null);
    return authService.logout(accessToken, customUserDetails.getUsername()); //username = email
  }

}