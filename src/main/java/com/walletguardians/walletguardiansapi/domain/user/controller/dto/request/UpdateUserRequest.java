package com.walletguardians.walletguardiansapi.domain.user.controller.dto.request;

import lombok.Getter;

@Getter
public class UpdateUserRequest {

  private String password;
  private String newPassword;

}
