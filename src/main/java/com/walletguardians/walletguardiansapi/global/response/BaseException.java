package com.walletguardians.walletguardiansapi.global.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//401에러, 404에러 등등 자주 쓰이는 에러에 대한 exception처리도 등록해놓자

@Data
@NoArgsConstructor
public class BaseException extends RuntimeException{

    public BaseResponseStatus status;

    public BaseException(BaseResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
