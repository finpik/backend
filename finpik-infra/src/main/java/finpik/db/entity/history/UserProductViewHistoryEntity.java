package finpik.db.entity.history;

import java.time.LocalDateTime;

import finpik.history.entity.UserProductViewHistory;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProductViewHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long productId;

    private LocalDateTime viewedAt;

    @Builder
    public UserProductViewHistoryEntity(UserProductViewHistory domain, LocalDateTime viewedAt) {
        this.userId = domain.userId();
        this.productId = domain.productId();
        this.viewedAt = viewedAt;
    }
}
