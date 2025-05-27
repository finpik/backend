package finpik.history.repository;

import finpik.history.entity.UserProductViewHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProductViewHistoryRepository {
    void save(UserProductViewHistory userProductViewHistory);
}
