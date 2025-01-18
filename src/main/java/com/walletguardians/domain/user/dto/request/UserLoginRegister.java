package com.walletguardians.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserLoginRegister {
  private String email;
  private String password;
}
