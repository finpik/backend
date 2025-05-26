package finpik.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import finpik.auth.repository.AuthCacheRepository;
import finpik.entity.enums.Gender;
import finpik.entity.enums.RegistrationType;
import finpik.error.enums.ErrorCode;
import finpik.user.entity.User;
import finpik.user.repository.UserRepository;
import finpik.user.service.dto.CreateUserDto;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthCacheRepository authCacheRepository;

    @DisplayName("유저를 생성할 수 있다.")
    @Test
    void createUser() {
        // given
        String email = "test@kakao.com";

        CreateUserDto dto = CreateUserDto.builder().username("테스트").dateOfBirth(LocalDate.of(2025, 5, 25))
                .gender(Gender.MALE).provider("kakao").id("12341234").build();

        User user = dto.toUser(email);

        when(authCacheRepository.getEmailByCustomId(dto.id(), dto.provider())).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        // when
        User result = userService.createUser(dto);

        // then
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @DisplayName("유저가 존재한다면 에러를 발생한다.")
    @Test
    void validateExistingUserBy() {
        // given
        String email = "test@kakao.com";

        CreateUserDto dto = CreateUserDto.builder().username("테스트").dateOfBirth(LocalDate.of(2025, 5, 25))
                .gender(Gender.MALE).provider("kakao").id("12341234").build();

        User user = dto.toUser(email);

        when(authCacheRepository.getEmailByCustomId(dto.id(), dto.provider())).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        // then
        assertThatThrownBy(() -> userService.createUser(dto)).hasMessage(ErrorCode.EXISTING_USER.getMessage());
    }

    @DisplayName("userId로 user를 검색할 수 있다.")
    @Test
    void findUserBy() {
        // given
        Long userId = 1L;
        User user = User.builder().id(userId).username("테스트").dateOfBirth(LocalDate.of(2025, 5, 25)).gender(Gender.MALE)
                .registrationType(RegistrationType.KAKAO).registrationDate(LocalDateTime.now()).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User result = userService.findUserBy(userId);

        // then
        assertThat(result.getId()).isEqualTo(user.getId());
    }
}
