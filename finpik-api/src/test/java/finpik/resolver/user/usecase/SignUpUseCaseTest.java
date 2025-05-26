package finpik.resolver.user.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import finpik.entity.enums.Gender;
import finpik.jwt.JwtProvider;
import finpik.resolver.user.usecase.dto.SignUpResultDto;
import finpik.resolver.user.usecase.dto.SignUpUseCaseDto;
import finpik.user.entity.User;
import finpik.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class SignUpUseCaseTest {
    @InjectMocks
    private SignUpUseCase signUpUseCase;

    @Mock
    private UserService userService;

    @Mock
    private JwtProvider jwtProvider;

    @DisplayName("sign up할 유저를 전달하면 유저정보와 함께 access token이 리턴된다.")
    @Test
    void signUp() {
        // given
        SignUpUseCaseDto dto = SignUpUseCaseDto.builder().username("테스트").dateOfBirth(LocalDate.of(2025, 5, 25))
                .gender(Gender.MALE).provider("123123").vendorId("kakao").issuedAt(Date.from(Instant.now()))
                .expiresAt(Date.from(Instant.now())).build();

        User user = User.builder().id(1L).email("test@test.com").dateOfBirth(LocalDate.of(2025, 5, 25))
                .gender(Gender.MALE).username("테스트").build();

        when(jwtProvider.createAccessToken(any())).thenReturn("testAccessToken");
        when(userService.createUser(dto.toCreateUserDto())).thenReturn(user);

        // when
        SignUpResultDto result = signUpUseCase.signUp(dto);

        // then
        assertAll(() -> assertThat(result.getUserId()).isEqualTo(1L),
                () -> assertThat(result.getAccessToken()).isEqualTo("testAccessToken"));
    }
}
