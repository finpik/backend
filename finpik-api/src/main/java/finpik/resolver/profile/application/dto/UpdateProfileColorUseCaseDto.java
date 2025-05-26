package finpik.resolver.profile.application.dto;

import finpik.entity.enums.ProfileColor;
import finpik.profile.service.dto.UpdateProfileColorDto;
import lombok.Builder;

@Builder
public record UpdateProfileColorUseCaseDto(Long id, ProfileColor profileColor) {
    public UpdateProfileColorDto toDomainDto() {
        return UpdateProfileColorDto.builder().id(id).profileColor(profileColor).build();
    }
}
