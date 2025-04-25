package com.loanpick.user.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

import com.loanpick.LoanPickApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loanpick.redis.service.CustomRedisService;
import com.loanpick.user.entity.Gender;
import com.loanpick.user.entity.User;
import com.loanpick.user.repository.UserRepository;
import com.loanpick.user.service.dto.CreateUserDto;

@SpringBootTest(classes = LoanPickApplication.class)
@ActiveProfiles("test") // test 설정 사용 시
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomRedisService customRedisService;

    @Test
    @DisplayName("유저 생성 시 Redis에서 email을 받아와 DB에 저장한다")
    void createUser_successfullyStoresUserWithEmailFromRedis() {
        // given
        CreateUserDto dto = CreateUserDto.builder().username("홍길동").dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE).provider("kakao").id("123456").build();

        customRedisService.saveEmailForSignUp("123456", "kakao", "test@kakao.com", Duration.ofMinutes(10));

        // when
        User savedUser = userService.createUser(dto);
        Optional<User> userFromDb = userRepository.findById(savedUser.getId());

        // then
        assertAll(() -> assertEquals("홍길동", savedUser.getUsername()),
                () -> assertEquals("test@kakao.com", savedUser.getEmail()), () -> assertTrue(userFromDb.isPresent()),
                () -> assertEquals("test@kakao.com", userFromDb.get().getEmail()));
    }
}
