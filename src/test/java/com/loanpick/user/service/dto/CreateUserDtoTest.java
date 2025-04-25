package com.loanpick.user.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.loanpick.user.entity.Gender;
import com.loanpick.user.entity.User;

@ActiveProfiles("test")
class CreateUserDtoTest {

    @DisplayName("서비스의 Dto로 유저 엔티티를 만들 수 있다.")
    @Test
    void toEntity() {
        // given
        CreateUserDto dto = CreateUserDto.builder().id("test").username("test name").dateOfBirth(LocalDate.now())
                .gender(Gender.MALE).provider("kakao").build();

        String email = "test@email.com";

        // when
        User entity = dto.toEntity(email);

        // then
        assertThat(entity).isInstanceOf(User.class);
    }
}
