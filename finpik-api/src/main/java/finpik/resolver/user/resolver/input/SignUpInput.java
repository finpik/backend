package finpik.resolver.user.resolver.input;

import java.time.LocalDate;
import java.util.Date;

import finpik.entity.enums.Gender;
import finpik.resolver.user.usecase.dto.SignUpUseCaseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignUpInput(
        @NotBlank(message = "이름을 넣어주세요.") @Size(min = 2, message = "이름은 최소 2글자 이상이어야 합니다.")
        String username,
        @NotNull(message = "생일을 넣어주세요.")
        LocalDate dateOfBirth,
        Gender gender,
        @NotBlank(message = "회원가입에 사용한 벤더사를 넣어주세요.")
        String provider,
        @NotBlank(message = "사용자 벤더 아이디를 넣어주세요.")
        String vendorId
) {

    public SignUpUseCaseDto toDto(Date issuedAt, Date expiresAt) {
        return SignUpUseCaseDto.builder()
            .username(username)
            .dateOfBirth(dateOfBirth)
            .gender(gender)
            .provider(provider)
            .vendorId(vendorId)
            .issuedAt(issuedAt)
            .expiresAt(expiresAt)
            .build();
    }
}
