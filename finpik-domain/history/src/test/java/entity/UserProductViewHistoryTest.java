package entity;

import finpik.entity.UserProductViewHistory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserProductViewHistoryTest {

    @DisplayName("기본 생성 테스트")
    @Test
    void create() {
        //given
        Long userId = 1L;
        Long productId = 1L;

        //when
        UserProductViewHistory userProductViewHistory = new UserProductViewHistory(userId, productId);

        //then
        Assertions.assertThat(userProductViewHistory.userId()).isEqualTo(userId);
        Assertions.assertThat(userProductViewHistory.loanProductId()).isEqualTo(productId);
    }
}
