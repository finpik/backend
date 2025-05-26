package finpik.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import finpik.entity.enums.Gender;
import finpik.entity.enums.RegistrationType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
    private final Long id;
    private final String username;
    private final String email;
    private final Gender gender;
    private final RegistrationType registrationType;
    private final LocalDate dateOfBirth;
    private final LocalDateTime registrationDate;

    //@formatter:off
    @Builder
    public User(
        Long id, String username, String email,
        Gender gender, RegistrationType registrationType,
        LocalDateTime registrationDate, LocalDate dateOfBirth
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.registrationType = registrationType == null ? RegistrationType.KAKAO : registrationType;
        this.registrationDate = registrationDate;
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
