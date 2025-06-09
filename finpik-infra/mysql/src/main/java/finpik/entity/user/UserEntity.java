package finpik.entity.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

import finpik.entity.enums.Gender;
import finpik.entity.enums.RegistrationType;
import finpik.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private RegistrationType registrationType;

    private LocalDate dateOfBirth;

    private LocalDateTime registrationDate;

    //@formatter:off
    @Builder
    public UserEntity(
        String username, String email,
        Gender gender, RegistrationType registrationType,
        LocalDateTime registrationDate, LocalDate dateOfBirth
    ) {
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.registrationType = registrationType == null ? RegistrationType.KAKAO : registrationType;
        this.registrationDate = registrationDate;
        this.dateOfBirth = dateOfBirth;
    }

    public static UserEntity from(User user) {
        return UserEntity.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .gender(user.getGender())
            .registrationType(user.getRegistrationType())
            .dateOfBirth(user.getDateOfBirth())
            .registrationDate(user.getRegistrationDate())
            .build();
    }

    public User toDomain() {
        return User.withId(
            id, username, email, gender,
            registrationType, registrationDate, dateOfBirth
        );
    }
}
