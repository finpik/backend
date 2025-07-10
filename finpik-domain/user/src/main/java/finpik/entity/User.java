package finpik.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import finpik.entity.enums.Gender;
import finpik.entity.enums.RegistrationType;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    private Long id;
    private String username;
    private String email;
    private Gender gender;
    private RegistrationType registrationType;
    private LocalDate dateOfBirth;
    private LocalDateTime registrationDate;

    public User(
        Long id, String username, String email,
        Gender gender, RegistrationType registrationType,
        LocalDateTime registrationDate, LocalDate dateOfBirth
    ) {
        validateEmailFormat(email);

        this.id = id;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.registrationType = registrationType == null ? RegistrationType.KAKAO : registrationType;
        this.registrationDate = registrationDate;
        this.dateOfBirth = dateOfBirth;
    }

    public static User from(
        String username, String email,
        Gender gender, String provider,
        LocalDate dateOfBirth
    ) {
        RegistrationType registrationType = RegistrationType.fromName(provider);
        LocalDateTime registrationDate = LocalDateTime.now();

        return new User(
            null, username, email,
            gender, registrationType,
            registrationDate, dateOfBirth
        );
    }

    public static User withId(
        Long id, String username, String email,
        Gender gender, RegistrationType registrationType,
        LocalDateTime registrationDate, LocalDate dateOfBirth
    ) {
        return new User(
            id, username, email,
            gender, registrationType,
            registrationDate, dateOfBirth
        );
    }

    private void validateEmailFormat(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (isEmailFormat(email, regex)) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL_FORMAT);
        }
    }

    private boolean isEmailFormat(String email, String regex) {
        return email == null || !email.matches(regex);
    }

    public Integer getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear() + 1;
    }
}
