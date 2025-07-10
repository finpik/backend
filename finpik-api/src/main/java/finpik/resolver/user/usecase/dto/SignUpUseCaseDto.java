package finpik.resolver.user.usecase.dto;

import java.time.LocalDate;
import java.util.Date;

import finpik.entity.User;
import finpik.entity.enums.Gender;
import lombok.Builder;

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

    public User toDomain(String email) {
        return User.from(
            username, email, gender,
            provider, dateOfBirth
        );
    }
}
