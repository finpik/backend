package com.loanpick.error.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // --------- 글로벌 에러처리 시작-------
    INTERNAL_SERVER_ERROR("서버 에러", "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    CONSTRAINT_VIOLATION_EXCEPTION("입력값 검증 실패", "CONSTRAINT_VIOLATION_EXCEPTION", HttpStatus.BAD_REQUEST),
    BIND_EXCEPTION("입력값 검증 실패", "BIND_EXCEPTION", HttpStatus.BAD_REQUEST),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION("입력값 검증 실패", "METHOD_ARGUMENT_NOT_VALID_EXCEPTION", HttpStatus.BAD_REQUEST),
    // --------- 글로벌 에러처리 끝-------

    // Test
    TEST_EXCEPTION("test용 에러", "TEST_EXCEPTION", HttpStatus.BAD_REQUEST),;

    private final String message;
    private final String code;
    private final HttpStatus status;
}
