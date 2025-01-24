package com.walletguardians.walletguardiansapi.global.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    SUCCESS(true, 2000, "요청에 성공했습니다."),

    NOT_FOUND_MEMBER_ID(false, 2001, "해당하는 회원 정보가 없습니다."),

    INVALID_EMAIL(false, 2002, "해당하는 이메일 정보가 없습니다."),

    /**
    * Calendar
    * Code : 4000번대
    */
     NOT_FOUND_CALENDAR_INFO(false, 4001, "캘린더가 존재하지 않습니다.");


    // -------- 실패 코드 종료 -------- //

    private boolean isSuccess; // 성공 여부
    private String message; // 메시지
    private int code; // 코드

    /**
     * BaseResponseStatus 에서 해당하는 코드를 매핑
     *
     * @param isSuccess
     * @param code
     * @param message
     */
    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
