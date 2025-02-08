package com.walletguardians.walletguardiansapi.domain.user.controller.dto.request;

import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.entity.auth.Role;
import lombok.Getter;

@Getter
public class UserRegisterRequest {
  private String email;
  private String password;
  private String username;

  public User toUserEntity() {
    return User.builder()
        .email(email)
        .password(password)
        .username(username)
        .role(Role.USER)
        .build();
  }
}
