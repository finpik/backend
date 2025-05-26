package finpik.profile.service.dto;

import finpik.entity.enums.ProfileColor;
import lombok.Builder;

@Builder
public record UpdateProfileColorDto(Long id, ProfileColor profileColor) {
}
