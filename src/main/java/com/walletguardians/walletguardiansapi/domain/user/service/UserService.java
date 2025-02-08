package com.walletguardians.walletguardiansapi.domain.user.service;

import com.walletguardians.walletguardiansapi.domain.user.dto.request.UserUpdateRequest;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  //포스트맨 id 값 없을 때
  public User findByUserId(Long userId) {
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
  public User updateUserProfile(String email, UserUpdateRequest request) {
    User user = findUserByEmail(email);
    if (request.getUsername() != null) {
      user.updateUsername(request.getUsername());
    }
    if (request.getTitle() != null) {
      user.updateTitle(request.getTitle());
    }
    return userRepository.save(user);
  }

}
