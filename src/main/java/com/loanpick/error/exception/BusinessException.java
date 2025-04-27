package com.loanpick.error.exception;

import java.util.Map;

import com.loanpick.error.enums.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public Map<String, Object> getExtensions() {
        return Map.of("code", errorCode.getCode(), "httpStatusErrorCode", errorCode.getStatus().name(),
                "httpStatusCode", errorCode.getStatus().value());
    }
}
