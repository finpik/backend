package finpik.resolver.user.usecase.dto;

import java.time.LocalDate;

import finpik.entity.User;
import finpik.entity.enums.Gender;
import lombok.Getter;

@Getter
public class SignUpResultDto {
    private final Long userId;
    private final String email;
    private final String username;
    private final LocalDate dateOfBirth;
    private final Gender gender;
    private final String accessToken;

    public SignUpResultDto(User user, String accessToken) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.dateOfBirth = user.getDateOfBirth();
        this.gender = user.getGender();
        this.accessToken = accessToken;
    }
}
