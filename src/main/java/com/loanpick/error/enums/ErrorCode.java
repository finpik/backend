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
    TEST_EXCEPTION("test용 에러", "TEST_EXCEPTION", HttpStatus.BAD_REQUEST),

    // User
    EXISTING_USER("이미 존재하는 유저입니다.", "USR-0001", HttpStatus.BAD_REQUEST),
    NOT_FOUND_USER("유저를 찾을 수 없습니다.", "USR-0002", HttpStatus.NOT_FOUND),

    // Profile
    EXCEEDING_PROFILE_COUNT_LIMIT("제한된 프로파일 개수보다 많이 생성할 수 없습니다.", "PRF-0001", HttpStatus.BAD_REQUEST),
    NOT_FOUND_PROFILE("프로필을 찾을 수 없습니다.", "PRF-0002", HttpStatus.NOT_FOUND),
    NOT_PROFILE_OWNER("해당 프로필을 가진 유저가 아닙니다.", "PRF-0003", HttpStatus.BAD_REQUEST),
    INVALID_EMPLOYMENT_INFO("직장인 선택시 관련 정보들이 전부 채워져야 합니다.", "PRF-0004", HttpStatus.BAD_REQUEST),
    INVALID_NOT_EMPLOYMENT_INFO("직장인이 아닌데 관련 정보들이 채워져 있으면 안됩니다.", "PRF-0005", HttpStatus.BAD_REQUEST),
    DUPLICATE_PROFILE_SEQ("프로필 순번이 중복이 될 수 없습니다.", "PRF-0006", HttpStatus.BAD_REQUEST),
    DUPLICATE_UPDATE_PROFILE_ID("프로필을 중복해서 업데이트 할 수 없습니다.", "PRF-0007", HttpStatus.BAD_REQUEST),;

    private final String message;
    private final String code;
    private final HttpStatus status;
}
