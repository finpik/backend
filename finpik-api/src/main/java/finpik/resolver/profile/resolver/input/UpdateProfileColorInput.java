package finpik.resolver.profile.resolver.input;

import finpik.entity.enums.ProfileColor;
import finpik.resolver.profile.application.dto.UpdateProfileColorUseCaseDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateProfileColorInput(
    @NotNull(message = "바꿀 프로필의 아이디가 필요합니다.")
    Long id,
    @NotNull(message = "지원하지 않는 색상입니다.")
    ProfileColor profileColor
) {
    public UpdateProfileColorUseCaseDto toDto() {
        return UpdateProfileColorUseCaseDto.builder().id(id).profileColor(profileColor).build();
    }
}
