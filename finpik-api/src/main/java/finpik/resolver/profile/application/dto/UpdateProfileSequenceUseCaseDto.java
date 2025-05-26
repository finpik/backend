package finpik.resolver.profile.application.dto;

import finpik.profile.service.dto.UpdateProfileSequenceDto;
import lombok.Builder;

@Builder
public record UpdateProfileSequenceUseCaseDto(Long id, Integer seq) {
    public UpdateProfileSequenceDto toDomainDto() {
        return UpdateProfileSequenceDto.builder().id(id).seq(seq).build();
    }
}
