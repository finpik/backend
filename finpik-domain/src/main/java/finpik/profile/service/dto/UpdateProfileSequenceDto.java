package finpik.profile.service.dto;

import lombok.Builder;

@Builder
public record UpdateProfileSequenceDto(Long id, Integer seq) {
}
