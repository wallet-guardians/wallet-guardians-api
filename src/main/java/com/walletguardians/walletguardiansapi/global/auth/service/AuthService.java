package com.walletguardians.walletguardiansapi.global.auth.service;

import com.walletguardians.walletguardiansapi.domain.user.controller.dto.request.UserLoginRequest;
import com.walletguardians.walletguardiansapi.domain.user.controller.dto.request.UserRegisterRequest;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.repository.UserRepository;
import com.walletguardians.walletguardiansapi.domain.user.service.UserService;
import com.walletguardians.walletguardiansapi.global.auth.jwt.dto.TokenDto;
import com.walletguardians.walletguardiansapi.global.auth.jwt.service.JwtService;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtService jwtService;
  private final UserService userService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void signUpUser(UserRegisterRequest userRegisterRequest) {
    User user = userRepository.save(userRegisterRequest.toUserEntity());
    user.encodePassword(passwordEncoder);
  }

  @Transactional
  public TokenDto login(UserLoginRequest userLoginRequest) {
    User user = userService.findUserByEmail(userLoginRequest.getEmail());

    if (!user.isPasswordValid(passwordEncoder, userLoginRequest.getPassword())) {
      throw new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID);
    }

    return jwtService.signIn(userLoginRequest.getEmail(), userLoginRequest.getPassword());
  }

}