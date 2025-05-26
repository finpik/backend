package finpik.resolver.user.usecase.dto;

import java.time.LocalDate;
import java.util.Date;

import finpik.entity.enums.Gender;
import finpik.user.service.dto.CreateUserDto;
import lombok.Builder;

//@formatter:off
@Builder
public record SignUpUseCaseDto(
    String username,
    LocalDate dateOfBirth,
    Gender gender,
    String provider,
    String vendorId,
    Date issuedAt,
    Date expiresAt
) {
    public CreateUserDto toCreateUserDto() {
        return CreateUserDto.builder()
            .username(username)
            .dateOfBirth(dateOfBirth)
            .gender(gender)
            .provider(provider)
            .id(vendorId)
            .build();
    }
}
