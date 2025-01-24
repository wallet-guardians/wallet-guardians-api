package com.walletguardians.walletguardiansapi.global.response;

public interface BaseResponseService {
   <T> BaseResponse<T> getSuccessResponse(T data);
   BaseResponse<Void> getSuccessResponse(); // 전달 데이터가 없는 경우
   BaseResponse<Void> getFailureResponse(BaseResponseStatus status); // 실패 응답
}
