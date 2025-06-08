package finpik.resolver.profile.application.dto;

import lombok.Builder;

@Builder
public record UpdateProfileSequenceUseCaseDto(Long id, Integer seq) {}
