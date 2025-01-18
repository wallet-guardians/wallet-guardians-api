package com.walletguardians.domain.user.entity.auth;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  private final String roles;

  public List<String> getRoles() {
    return Arrays.asList(roles.split(","));
  }
}