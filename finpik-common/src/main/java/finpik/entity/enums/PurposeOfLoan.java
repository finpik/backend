package finpik.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurposeOfLoan {
    LIVING_EXPENSES("생활비"), TUITION("학자금 / 등록금"), HOUSING("주거"), BUSINESS_FUNDS("창업 / 사업 운영 자금"),
    MEDICAL_OR_EMERGENCY("의료비 / 비상 상황 대비"), LOAN_REPAYMENT("기존 대출 상환"),;

    private final String name;
}
