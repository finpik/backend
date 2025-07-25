package finpik.entity.history.userproductview;

import java.time.LocalDateTime;

import finpik.entity.UserProductViewHistory;
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

    private Long loanProductId;

    private LocalDateTime viewedAt;

    @Builder
    public UserProductViewHistoryEntity(UserProductViewHistory domain, LocalDateTime viewedAt) {
        this.userId = domain.userId();
        this.loanProductId = domain.loanProductId();
        this.viewedAt = viewedAt;
    }
}
