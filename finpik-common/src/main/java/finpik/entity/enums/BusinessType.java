package finpik.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessType {
    RETAIL("소매업"),
    MEDICAL("의료업"), // 병원, 약국 포함
    FRANCHISE("프랜차이즈 가맹점"),
    RESTAURANT("음식점"),
    ONLINE_SHOP("온라인 쇼핑몰"),
    ETC("기타");

    private final String description;
}
