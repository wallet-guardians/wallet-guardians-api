package com.walletguardians.walletguardiansapi.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserLoginRegister {
  private String email;
  private String password;
}
