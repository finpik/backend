package finpik.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Occupation {
    EMPLOYEE("직장인"),
    PUBLIC_SERVANT("공무원"),
    SELF_EMPLOYED("사업자"),
    OTHER("프리랜서"),
    UNEMPLOYED("기타 (학생, 무직, 주부 등)"),
    ;

    private final String displayName;
}
