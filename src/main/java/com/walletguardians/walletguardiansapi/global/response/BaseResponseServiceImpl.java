package com.walletguardians.walletguardiansapi.global.response;

import org.springframework.stereotype.Component;

@Component
public class BaseResponseServiceImpl implements BaseResponseService {
    @Override
    public <T> BaseResponse<T> getSuccessResponse(T data) {
        return BaseResponse.<T>builder()
                .isSuccess(true)
                .code(BaseResponseStatus.SUCCESS.getCode())
                .message(BaseResponseStatus.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    @Override
    public BaseResponse<Void> getSuccessResponse() {
        return BaseResponse.<Void>builder()
                .isSuccess(true)
                .code(BaseResponseStatus.SUCCESS.getCode())
                .message(BaseResponseStatus.SUCCESS.getMessage())
                .build();
    }

    @Override
    public BaseResponse<Void> getFailureResponse(BaseResponseStatus status) {
        return BaseResponse.<Void>builder()
                .isSuccess(status.isSuccess())
                .code(status.getCode())
                .message(status.getMessage())
                .build();
    }
}
