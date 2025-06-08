package finpik.resolver.profile.application.dto;

import finpik.entity.enums.ProfileColor;
import lombok.Builder;

@Builder
public record UpdateProfileColorUseCaseDto(Long id, ProfileColor profileColor) {
}
