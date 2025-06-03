package finpik.error.exception;

import java.util.Map;

import finpik.error.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String message;
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.errorCode = errorCode;
    }

    public Map<String, Object> getExtensions() {
        return Map.of("code", errorCode.getCode(), "httpStatusErrorCode", errorCode.getStatus().name(),
                "httpStatusCode", errorCode.getStatus().value());
    }
}
