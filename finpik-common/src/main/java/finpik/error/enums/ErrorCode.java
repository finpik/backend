package finpik.error.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // --------- 글로벌 에러처리 시작-------
    INTERNAL_SERVER_ERROR("서버 에러", "INTERNAL_SERVER_ERROR", CustomHttpStatus.INTERNAL_SERVER_ERROR),
    CONSTRAINT_VIOLATION_EXCEPTION("입력값 검증 실패", "CONSTRAINT_VIOLATION_EXCEPTION", CustomHttpStatus.BAD_REQUEST),
    BIND_EXCEPTION("입력값 검증 실패", "BIND_EXCEPTION", CustomHttpStatus.BAD_REQUEST),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION("입력값 검증 실패", "METHOD_ARGUMENT_NOT_VALID_EXCEPTION", CustomHttpStatus.BAD_REQUEST),
    NULL_ARGUMENT_EXCEPTION("인자가 null 일경우 에러", "NULL_ARGUMENT_EXCEPTION", CustomHttpStatus.BAD_REQUEST),
    // --------- 글로벌 에러처리 끝-------

    // Test
    TEST_EXCEPTION("test용 에러", "TEST_EXCEPTION", CustomHttpStatus.BAD_REQUEST),

    // Auth
    INVALID_REFRESH_TOKEN("유효한 Refresh Token이 아닙니다.", "ATH-0001", CustomHttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("유효한 Access Token이 아닙니다. 재발급 해주세요.", "ATH-0002", CustomHttpStatus.UNAUTHORIZED),
    OAUTH2_AUTHORIZATION_FAIL("OAuth2 인증에 실패 했습니다.", "ATH-0003", CustomHttpStatus.UNAUTHORIZED),
    INTERNAL_AUTHORIZATION_FAIL("인증에 실패했습니다. 관리자에게 문의해주세요", "ATH-0004", CustomHttpStatus.INTERNAL_SERVER_ERROR),
    UNAVAILABLE_LOGIN_WAY("지원하지 않는 로그인 방식입니다.", "ATH-0005", CustomHttpStatus.UNAUTHORIZED ),

    // User
    EXISTING_USER("이미 존재하는 유저입니다.", "USR-0001", CustomHttpStatus.BAD_REQUEST),
    NOT_FOUND_USER("유저를 찾을 수 없습니다.", "USR-0002", CustomHttpStatus.NOT_FOUND),
    INVALID_EMAIL_FORMAT("이메일 포맷을 확인해 주세요", "USR-0003", CustomHttpStatus.BAD_REQUEST),

    // Profile
    EXCEEDING_PROFILE_COUNT_LIMIT("제한된 프로파일 개수보다 많이 생성할 수 없습니다.", "PRF-0001", CustomHttpStatus.BAD_REQUEST),
    NOT_FOUND_PROFILE("프로필을 찾을 수 없습니다.", "PRF-0002", CustomHttpStatus.NOT_FOUND),
    NOT_PROFILE_OWNER("해당 프로필을 가진 유저가 아닙니다.", "PRF-0003", CustomHttpStatus.BAD_REQUEST),
    INVALID_EMPLOYMENT_FIELDS("직장인 직군 관련 필드에 문제가 있습니다.", "PRF-0004", CustomHttpStatus.BAD_REQUEST),
    INVALID_SELF_EMPLOYED_FIELDS("사업자 직군 관련 필드에 문제가 있습니다.", "PRF-0005", CustomHttpStatus.BAD_REQUEST),
    DUPLICATE_PROFILE_SEQ("프로필 순번이 중복이 될 수 없습니다.", "PRF-0006", CustomHttpStatus.BAD_REQUEST),
    DUPLICATE_UPDATE_PROFILE_ID("프로필을 중복해서 업데이트 할 수 없습니다.", "PRF-0007", CustomHttpStatus.BAD_REQUEST),
    PROFILE_SEQUENCE_CANNOT_BE_NULL("프로필의 순번이 NULL일 수 없습니다.", "PRF-0008", CustomHttpStatus.BAD_REQUEST),
    CREDITS_CANNOT_BE_NULL("프로필의 신용점수, 신용상태 둘다 NULL일 수 없습니다.", "PRF-0009", CustomHttpStatus.BAD_REQUEST),
    OUT_OF_RANGE_CREDIT_GRADE_STATUS("신용점수에 맞는 신용 등급이 존재하지 않습니다.", "PRF-0010", CustomHttpStatus.BAD_REQUEST),
    INVALID_ANNUAL_INCOME("유효하지 않은 연수입니다. 값을 확인해주세요.", "PRF-0011", CustomHttpStatus.BAD_REQUEST),
    INVALID_PUBLIC_SERVANT_FIELDS("공무워 직군 관련 필드에 문제가 있습니다.", "PRF-0012", CustomHttpStatus.BAD_REQUEST),
    INVALID_FREELANCER_FIELDS("프리랜서 직군 관련 필드에 문제가 있습니다.", "PRF-0013", CustomHttpStatus.BAD_REQUEST),
    UNSUPPORTED_OCCUPATION("지원하지 않는 직군입니다.", "PRF-0014", CustomHttpStatus.BAD_REQUEST),

    // Loan Product
    NOT_FOUND_LOAN_PRODUCT("대출 상품을 찾을 수 없습니다.", "LPD-0001", CustomHttpStatus.NOT_FOUND),
    NOT_CONVERT_KAFKA_MESSAGE_LOAN_PRODUCT("카프카에서 받은 추천 대출 상품 메세지를 컨버팅할 수 없습니다.", "LPD-0002", CustomHttpStatus.BAD_REQUEST),
    NOT_CONVERT_KAFKA_MESSAGE("카프카에 메세지를 보내기 위한 추천 대출 상품의 조건을 문자열로 변경이 실패했습니다.", "LPD-0003", CustomHttpStatus.BAD_REQUEST),
    EMPTY_BADGES("추천된 상품에 뱃지를 받은 상품이 존재하지 않습니다.", "LPD-0004", CustomHttpStatus.BAD_REQUEST),

    //System
    ORDER_BY_PROPERTY_NOT_FOUND("정렬 조건을 찾을 수 없습니다. 개발자에게 문의 부탁드립니다.", "ITN-0001", CustomHttpStatus.INTERNAL_SERVER_ERROR),;

    private final String message;
    private final String code;
    private final CustomHttpStatus status;
}
