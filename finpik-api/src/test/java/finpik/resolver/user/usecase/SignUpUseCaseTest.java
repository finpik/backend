package finpik.resolver.user.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import finpik.entity.User;
import finpik.repository.auth.AuthCacheRepository;
import finpik.entity.enums.RegistrationType;
import finpik.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import finpik.JwtProvider;
import finpik.entity.enums.Gender;
import finpik.resolver.user.usecase.dto.SignUpResultDto;
import finpik.resolver.user.usecase.dto.SignUpUseCaseDto;

@ExtendWith(MockitoExtension.class)
class SignUpUseCaseTest {
    @InjectMocks
    private SignUpUseCase signUpUseCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private AuthCacheRepository authCacheRepository;

    @DisplayName("sign up할 유저를 전달하면 유저정보와 함께 access token이 리턴된다.")
    @Test
    void signUp() {
        // given
        SignUpUseCaseDto dto = SignUpUseCaseDto.builder().username("테스트").dateOfBirth(LocalDate.of(2025, 5, 25))
                .gender(Gender.MALE).provider("kakao").vendorId("123123").issuedAt(Date.from(Instant.now()))
                .expiresAt(Date.from(Instant.now())).build();

        Long userId = 1L;
        String email = "test@test.com";

        User user = User.withId(
            userId, "test", email, Gender.MALE,
            RegistrationType.KAKAO, LocalDateTime.now(), LocalDate.of(2025, 5, 25));

        when(jwtProvider.createAccessToken(any())).thenReturn("testAccessToken");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(authCacheRepository.getEmailByCustomId("123123", "kakao")).thenReturn(email);

        // when
        SignUpResultDto result = signUpUseCase.signUp(dto);

        // then
        assertAll(() -> assertThat(result.getUserId()).isEqualTo(1L),
                () -> assertThat(result.getAccessToken()).isEqualTo("testAccessToken"));
    }
}
