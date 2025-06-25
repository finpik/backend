package finpik.entity.history.sse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SseFailHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private UUID eventId;
    private String jsonData;
    @Column(updatable = false, nullable = false)
    private LocalDateTime failedAt;

    public static SseFailHistory of(UUID eventId, String jsonData, LocalDateTime failedAt) {
        return new SseFailHistory(null, eventId, jsonData, failedAt);
    }
}
