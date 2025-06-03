package finpik.jpa.repository.history.userproductview;

import org.springframework.data.jpa.repository.JpaRepository;

import finpik.entity.history.userproductview.UserProductViewHistoryEntity;

public interface UserProductViewHistoryJpaRepository
        extends
            JpaRepository<UserProductViewHistoryEntity, Long>,
            CustomUserProductViewHistoryRepository {
}
