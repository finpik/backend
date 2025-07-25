package finpik.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Occupation {
    EMPLOYEE("직장인"),
    PUBLIC_SERVANT("공무원"),
    SELF_EMPLOYED("사업자"),
    FREELANCER("프리랜서"),
    OTHER("기타 (학생, 무직, 주부 등)"),
    ALL("모든 대상")
    ;

    private final String displayName;
}
