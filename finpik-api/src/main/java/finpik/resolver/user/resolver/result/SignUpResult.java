package finpik.resolver.user.resolver.result;

import java.time.LocalDate;

import finpik.entity.enums.Gender;
import finpik.resolver.user.usecase.dto.SignUpResultDto;
import lombok.Builder;

@Builder
public record SignUpResult(
    Long userId,
    String email,
    String username,
    LocalDate dateOfBirth,
    Gender gender,
    String accessToken
) {
    public static SignUpResult of(SignUpResultDto dto) {
        return SignUpResult.builder()
            .userId(dto.getUserId())
            .email(dto.getEmail())
            .username(dto.getUsername())
            .dateOfBirth(dto.getDateOfBirth())
            .gender(dto.getGender())
            .accessToken(dto.getAccessToken())
            .build();
    }
}
