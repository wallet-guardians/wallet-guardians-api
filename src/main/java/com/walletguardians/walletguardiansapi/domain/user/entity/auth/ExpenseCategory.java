package com.walletguardians.walletguardiansapi.domain.user.entity.auth;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExpenseCategory {
  FOOD("식비", List.of(
      new TitleCondition(100, "식비만 100번 결제하셨습니다..."),
      new TitleCondition(20, "먹는 게 제일 좋아"),
      new TitleCondition(5, "가끔은 이런 것도 먹어 주는 게")
  )),
  SHOPPING("쇼핑", List.of(
      new TitleCondition(20, "잠시 눈을 감고 생각해 봅시다"),
      new TitleCondition(10, "이건 꼭 사야 해...?"),
      new TitleCondition(5, "어머, 이건 꼭 사야 해")
  )),
  ENTERTAIMENT("취미/여가", List.of(
      new TitleCondition(20, "도파민 중독 신고 번호는 1..."),
      new TitleCondition(10, "도파민에 숙성 중"),
      new TitleCondition(5, "재밌어. 짜릿해")
  )),
  ETC("기타", List.of(
      new TitleCondition(15, "카테고리 추가는 DM으로 분의주세요"),
      new TitleCondition(5, "예상을 벗어난 소비")
  )),
  TRANSPORTATION("교통비", List.of(
      new TitleCondition(10, "바쁘다 바빠. 현대 사회"),
      new TitleCondition(20, "택시비는 아니죠?")
  ));

  private final String expenseCategory;
  private final List<TitleCondition>;

  public static ExpenseCategory fromString(String category) {
    return Arrays.stream(values())
        .filter(c -> c.expenseCategory.equals(category))
        .findFirst()
        .orElse(null);
  }

}
