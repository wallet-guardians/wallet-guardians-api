package com.walletguardians.walletguardiansapi.global.exception;

import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseException extends RuntimeException{

    public BaseResponseStatus status;

    public BaseException(BaseResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
