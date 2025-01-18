package com.walletguardians.domain.user.dto.request;

import com.walletguardians.domain.user.entity.User;
import com.walletguardians.domain.user.entity.auth.Role;
import lombok.Getter;

@Getter
public class UserRegisterRequest {
  private String email;
  private String password;
  private String name;
  private String title;
  private float defenseRate;
  private boolean userDeleted;


  public User toUserEntity() {
    return User.builder()
        .email(email)
        .password(password)
        .name(name)
        .role(Role.USER)
        .title(title)
        .defenseRate(defenseRate)
        .userDeleted(userDeleted)
        .build();
  }

}
