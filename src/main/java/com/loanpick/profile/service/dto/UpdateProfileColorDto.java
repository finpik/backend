package com.loanpick.profile.service.dto;

import com.loanpick.profile.entity.enums.ProfileColor;

import lombok.Builder;

@Builder
public record UpdateProfileColorDto(Long id, ProfileColor profileColor) {
}
