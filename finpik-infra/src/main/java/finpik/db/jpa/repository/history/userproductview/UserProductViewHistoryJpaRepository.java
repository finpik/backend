package finpik.db.jpa.repository.history.userproductview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finpik.db.entity.history.userproductview.UserProductViewHistoryEntity;

@Repository
public interface UserProductViewHistoryJpaRepository
        extends
            JpaRepository<UserProductViewHistoryEntity, Long>,
            CustomUserProductViewHistoryRepository {
}
