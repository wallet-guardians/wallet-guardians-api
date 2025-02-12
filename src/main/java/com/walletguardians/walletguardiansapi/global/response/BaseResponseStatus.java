package com.walletguardians.walletguardiansapi.global.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

  SUCCESS(true, 2000, "요청에 성공했습니다.", 200),
  NO_FRIEND_REQUEST(false, 4000, "존재하는 친구 요청이 없습니다", 400),
  UNAUTHORIZED(false, 4001, "인증에 실패했습니다.", 401),
  NOT_FOUND_MEMBER_ID(false, 4002, "해당하는 회원 정보가 없습니다.", 404),
  DUPLICATE_MEMBER(false, 4003, "이미 존재하는 회원입니다.", 409),
  SAME_PASSWORD(false, 4004, "기존의 비밀번호와 동일합니다", 409),
  EXIST_BUDGET(false, 4005, "이미 예산이 설정되어 있습니다", 409),
  NO_BUDGET(false, 4006, "예산 정보가 없습니다.", 409),
  NO_EXPENSES(false, 4007, "지출 내역이 없습니다.", 409),
  NO_INCOME(false, 4008, "수입 내역이 없습니다.", 409),
  ALREADY_FRIEND_REQUEST_SENT_OR_FRIEND(false, 4009, "이미 존재하는 친구요청이거나 친구입니다.", 409),
  ALREADY_REJECTED(false, 4010, "이미 거절된 친구입니다.", 409),
  NO_FRIEND(false, 4011, "존재하지 않는 친구입니다.", 404),
  INVALID_FRIEND_STATUS(false, 4012, "잘못된 친구 요청 상태입니다.", 400),
  CAN_NOT_SEND_YOURSELF(false, 4013, "본인에게는 보낼 수 없습니다.", 409),
  INTERNAL_SERVER_ERROR(false, 5000, "서버 내부 오류가 발생했습니다.", 500);

  // 필드 정의
  private final boolean isSuccess; // 성공 여부
  private final int code; // 커스텀 상태 코드
  private final String message; // 메시지
  private final int httpStatus; // HTTP 상태 코드 ex)401, 404...

  BaseResponseStatus(boolean isSuccess, int code, String message, int httpStatus) {
    this.isSuccess = isSuccess;
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
