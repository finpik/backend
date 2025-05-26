package finpik.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import finpik.error.enums.ErrorCode;

class UserUtilTest {

    @DisplayName("유저가 null일 경우 INVALID_ACCESS_TOKEN에러가 발생한다.")
    @Test
    void require() {
        // given
        // when
        // then
        assertThatThrownBy(() -> UserUtil.require(null)).hasMessage(ErrorCode.INVALID_ACCESS_TOKEN.getMessage());
    }
}
