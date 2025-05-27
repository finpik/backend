package finpik.history.handler;

import finpik.dto.UserProductViewEvent;
import finpik.history.entity.UserProductViewHistory;
import finpik.history.repository.UserProductViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProductViewHistoryEventHandler {
    private final UserProductViewHistoryRepository userProductViewHistoryRepository;

    @Async
    @EventListener
    public void userProductViewEventHandle(UserProductViewEvent event) {
        UserProductViewHistory userProductViewHistory = new UserProductViewHistory(event.userId(), event.productId());

        userProductViewHistoryRepository.save(userProductViewHistory);
    }
}
