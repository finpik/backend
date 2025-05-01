package com.loanpick.profile.service.dto;

import lombok.Builder;

@Builder
public record UpdateProfileSequenceDto(long id, int seq) {
}
