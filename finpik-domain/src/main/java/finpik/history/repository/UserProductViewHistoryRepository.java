package finpik.history.repository;

import org.springframework.stereotype.Repository;

import finpik.history.entity.UserProductViewHistory;

@Repository
public interface UserProductViewHistoryRepository {
    void save(UserProductViewHistory userProductViewHistory);
}
