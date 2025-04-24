package com.loanpick.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class User {
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

    @Builder
    public User(Long id, String username, String email, Gender gender, RegistrationType registrationType,
            LocalDateTime registrationDate, LocalDate dateOfBirth) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.registrationType = registrationType == null ? RegistrationType.KAKAO : registrationType;
        this.registrationDate = registrationDate;
        this.dateOfBirth = dateOfBirth;
    }
}
