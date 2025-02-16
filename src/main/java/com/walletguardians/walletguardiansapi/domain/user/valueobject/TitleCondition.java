package com.walletguardians.walletguardiansapi.domain.user.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TitleCondition {

  private int threshold;
  private String title;

}

