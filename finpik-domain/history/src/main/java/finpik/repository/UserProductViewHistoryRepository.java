package finpik.repository;

import org.springframework.stereotype.Repository;

import finpik.entity.UserProductViewHistory;

@Repository
public interface UserProductViewHistoryRepository {
    void save(UserProductViewHistory userProductViewHistory);
}
