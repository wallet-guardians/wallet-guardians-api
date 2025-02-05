package com.walletguardians.walletguardiansapi.domain.user.service;

import com.walletguardians.walletguardiansapi.domain.user.controller.dto.request.UpdateUserRequest;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.repository.UserRepository;
import com.walletguardians.walletguardiansapi.global.auth.jwt.service.JwtService;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  //포스트맨 id 값 없을 때
  @Transactional(readOnly = true)
  public User findUserByUserId(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("해당하는 회원 정보가 없습니다."));
  }

  //로그인 기능일 때
  @Transactional(readOnly = true)
  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("해당하는 이메일 정보가 없습니다."));
  }

  @Transactional
  public void logout(String accessToken, String email) {
    boolean expiration = jwtService.validateToken(accessToken);
    if (expiration) {
      jwtService.deleteRefreshTokenByEmail(email);
    } else {
      throw new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID);
    }
  }

  @Transactional
  public void deleteById(Long userId) {
    userRepository.deleteById(userId);
  }

  @Transactional
  public User updatePassword(Long userId, UpdateUserRequest updateUserRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID));

    if (!user.isPasswordValid(passwordEncoder, updateUserRequest.getPassword())) {
      throw new BaseException(BaseResponseStatus.UNAUTHORIZED);
    }

    if (user.isPasswordValid(passwordEncoder, updateUserRequest.getNewPassword())) {
      throw new BaseException(BaseResponseStatus.SAME_PASSWORD);
    }

    user.updatePassword(passwordEncoder, updateUserRequest.getNewPassword());
    return user;
  }

}
