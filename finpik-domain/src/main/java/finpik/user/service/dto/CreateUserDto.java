package finpik.user.service.dto;

import java.time.LocalDate;

import finpik.entity.enums.Gender;
import finpik.entity.enums.RegistrationType;
import finpik.user.entity.User;
import lombok.Builder;

@Builder
public record CreateUserDto(String username, LocalDate dateOfBirth, Gender gender, String provider, String id) {

    public User toUser(String email) {
        return User.builder().username(username).dateOfBirth(dateOfBirth).gender(gender).email(email)
                .registrationType(RegistrationType.fromName(provider)).build();
    }
}
