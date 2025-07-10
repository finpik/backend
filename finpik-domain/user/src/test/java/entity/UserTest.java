package entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;

import finpik.entity.User;
import finpik.entity.enums.Gender;
import finpik.entity.enums.RegistrationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @DisplayName("유저를 생성할 수 있다. 생성하면 provider 문자열을 알맞은 RegistrationType으로 변경한다.")
    @Test
    void fromTest() {
        //given
        //when
        User user = User.from(
            "test", "test@test.com", Gender.MALE,
            "kakao", LocalDate.of(1993, 12, 14)
        );

        //then
        assertAll(
            () -> assertThat(user.getId()).isEqualTo(null),
            () -> assertThat(user.getRegistrationType()).isEqualTo(RegistrationType.KAKAO)
        );
    }

    @DisplayName("유저 객체의 나이를 계산할 수 있다.")
    @Test
    void getAge() {
        // given
        User user = User.withId(
            1L, "test", "test@test.com", Gender.MALE,
            RegistrationType.KAKAO, LocalDateTime.now(), LocalDate.of(1993, 12, 14)
        );

        // when
        Integer result = user.getAge();

        // then
        assertThat(result).isEqualTo(33);
    }
}
