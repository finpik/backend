package com.loanpick.profile.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmploymentStatus {
    EMPLOYEE("직장인"),
    SELF_EMPLOYED("사업자"),
    PUBLIC_SERVANT("공무원"),
    UNEMPLOYED("무직 (주부 등)"),
    OTHER("기타 (학생, 프리랜서 등)");

    private final String displayName;
}
