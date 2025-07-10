package finpik.jpa.repository.history.userproductview;

import java.time.LocalDateTime;

import finpik.entity.UserProductViewHistory;
import finpik.repository.UserProductViewHistoryRepository;
import org.springframework.stereotype.Repository;

import finpik.entity.history.userproductview.UserProductViewHistoryEntity;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserProductViewHistoryRepositoryImpl implements UserProductViewHistoryRepository {
    private final UserProductViewHistoryJpaRepository repository;

    @Override
    public void save(UserProductViewHistory domain) {
        LocalDateTime viewedAt = LocalDateTime.now();

        UserProductViewHistoryEntity entity = new UserProductViewHistoryEntity(domain, viewedAt);

        repository.save(entity);
    }
}
