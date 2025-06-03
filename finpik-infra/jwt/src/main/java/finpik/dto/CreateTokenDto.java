package finpik.dto;

import java.util.Date;

import lombok.Builder;

@Builder
public record CreateTokenDto(Long userId, String email, Date issuedAt, Date expiration) {
}
