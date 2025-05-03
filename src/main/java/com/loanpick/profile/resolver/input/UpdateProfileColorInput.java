package com.loanpick.profile.resolver.input;

import com.loanpick.profile.entity.enums.ProfileColor;
import com.loanpick.profile.service.dto.UpdateProfileColorDto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateProfileColorInput(@NotNull(message = "바꿀 프로필의 아이디가 필요합니다.") Long id,
        @NotNull(message = "지원하지 않는 색상입니다.") ProfileColor profileColor) {
    public UpdateProfileColorDto toDto() {
        return UpdateProfileColorDto.builder().id(id).profileColor(profileColor).build();
    }
}
