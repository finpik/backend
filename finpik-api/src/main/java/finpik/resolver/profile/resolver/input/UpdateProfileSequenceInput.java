package finpik.resolver.profile.resolver.input;

import finpik.resolver.profile.application.dto.UpdateProfileSequenceUseCaseDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

//@formatter:off
@Builder
public record UpdateProfileSequenceInput(
    @NotNull(message = "프로필 id가 필요합니다.")
    Long id,
    @NotNull(message = "프로필 순번이 필요합니다.")
    @Max(value = 3, message = "프로필 순번의 제한은 4개까지 입니다.")
    @Min(value = 0, message = "프로필은 0번부터 시작할 수 있습니다.")
    Integer seq
) {
    public UpdateProfileSequenceUseCaseDto toDto() {
        return UpdateProfileSequenceUseCaseDto.builder()
            .id(id)
            .seq(seq)
            .build();
    }
}
