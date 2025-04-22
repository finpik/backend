package com.loanpick.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
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

  private LocalDateTime registrationDate;

  @Builder
  public User(
      Long id,
      String username,
      String email,
      Gender gender,
      RegistrationType registrationType,
      LocalDateTime registrationDate) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.gender = gender;
    this.registrationType = registrationType == null ? RegistrationType.KAKAO : registrationType;
    this.registrationDate = registrationDate;
  }
}
