package finpik.db.jpa.repository.history;

import finpik.db.entity.history.UserProductViewHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProductViewHistoryJpaRepository
    extends JpaRepository<UserProductViewHistoryEntity, Long>, CustomUserProductViewHistoryRepository{
}
