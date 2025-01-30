package com.walletguardians.walletguardiansapi.global.exception;

import com.walletguardians.walletguardiansapi.global.response.BaseResponse;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseService;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import io.jsonwebtoken.JwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final BaseResponseService baseResponseService;

    public GlobalExceptionHandler(BaseResponseService baseResponseService) {
        this.baseResponseService = baseResponseService;
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<Void>> handleBaseException(BaseException ex) {
        BaseResponseStatus status = ex.getStatus();
        BaseResponse<Void> response = baseResponseService.getFailureResponse(status);
        return ResponseEntity.status(status.getHttpStatus()).body(response);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<BaseResponse<Void>> handleJwtException(JwtException ex) {
        BaseResponse<Void> response = baseResponseService.getFailureResponse(BaseResponseStatus.UNAUTHORIZED);
        return ResponseEntity.status(401).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        BaseResponse<Void> response = baseResponseService.getFailureResponse(BaseResponseStatus.DUPLICATE_MEMBER);
        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse<Void>> handleRuntimeException(RuntimeException ex) {
        BaseResponse<Void> response = baseResponseService.getFailureResponse(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(500).body(response);
    }
}
