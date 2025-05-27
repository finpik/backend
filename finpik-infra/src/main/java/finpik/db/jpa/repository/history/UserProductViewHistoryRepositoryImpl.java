package finpik.db.jpa.repository.history;

import finpik.db.entity.history.UserProductViewHistoryEntity;
import finpik.history.entity.UserProductViewHistory;
import finpik.history.repository.UserProductViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

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
