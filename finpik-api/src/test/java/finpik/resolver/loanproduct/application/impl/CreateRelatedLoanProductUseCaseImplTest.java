package finpik.resolver.loanproduct.application.impl;

import finpik.history.entity.UserProductViewHistory;
import finpik.history.repository.UserProductViewHistoryRepository;
import finpik.resolver.loanproduct.application.CreateRelatedLoanProductUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateRelatedLoanProductUseCaseImplTest {
    @InjectMocks
    private CreateRelatedLoanProductUseCaseImpl createRelatedLoanProductUseCase;

    @Mock
    private UserProductViewHistoryRepository userProductViewHistoryRepository;

    @DisplayName("이벤트가 들어오면 리슨하여 repository에 save가 호출되고 파라미터가 일치한다.")
    @Test
    void userProductViewEventHandle() {
        // given
        Long userId = 1L;
        Long productId = 2L;
        ArgumentCaptor<UserProductViewHistory> captor = ArgumentCaptor.forClass(UserProductViewHistory.class);

        // when
        createRelatedLoanProductUseCase.createUserProductViewAsync(userId, productId);

        // then
        verify(userProductViewHistoryRepository).save(captor.capture());
        UserProductViewHistory userProductViewHistory = captor.getValue();

        assertAll(() -> assertThat(userId).isEqualTo(userProductViewHistory.userId()),
            () -> assertThat(productId).isEqualTo(userProductViewHistory.productId()));
    }
}
