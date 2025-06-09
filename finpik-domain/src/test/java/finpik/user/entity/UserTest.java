package finpik.user.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import finpik.entity.enums.Gender;
import finpik.entity.enums.RegistrationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

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
