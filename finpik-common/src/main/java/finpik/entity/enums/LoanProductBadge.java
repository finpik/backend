package finpik.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoanProductBadge {
    LOWEST_MIN_INTEREST_RATE("최저금리가 제일 낮은 상품"),
    BEST_PROFILE_MATCH("프로필의 조건과 제일 잘 맞아요"),
    HIGHEST_MAX_LOAN_AMOUNT_LIMIT("최대한도가 가장 높아요"),
    INSTANT_DEPOSIT("즉시 입금 가능해요");

    private final String description;
}
