package com.walletguardians.walletguardiansapi.global.auth.service;

import com.walletguardians.walletguardiansapi.domain.user.dto.request.UserLoginRegister;
import com.walletguardians.walletguardiansapi.domain.user.dto.request.UserRegisterRequest;
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
  public TokenDto login(UserLoginRegister userLoginRegister) {
    User user = userService.findUserByEmail(userLoginRegister.getEmail());

    if (!user.isPasswordValid(passwordEncoder, userLoginRegister.getPassword())) {
      throw new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID);
    }

    return jwtService.signIn(userLoginRegister.getEmail(), userLoginRegister.getPassword());
  }

  @Transactional
  public void logout(String accessToken, String email) {
    boolean expiration = jwtService.validateToken(accessToken);
    if (expiration) {
      jwtService.deleteRefreshToken(email);
    } else {
      throw new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID);
    }
  }
}
