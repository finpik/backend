package finpik.entity.history.dlq;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class KafkaFailedMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    /**
     * Kafka 메시지 값 (payload)
     */
    @Lob
    @Column(nullable = false)
    private String payload;

    /**
     * 실패 사유
     */
    @Lob
    @Column(nullable = false)
    private String errorMessage;

    /**
     * 상태 (DLQ_SENT, RETRIED, RETRY_FAILED 등)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    /**
     * 생성 시간
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * 재처리 성공 시간
     */
    private LocalDateTime retriedAt;

    public enum Status {
        DLQ_SENT, RETRIED, RETRY_FAILED
    }

    public static KafkaFailedMessage from(String topic, String payload, String errorMessage, Status status,
            LocalDateTime createdAt, LocalDateTime retriedAt) {
        return new KafkaFailedMessage(topic, payload, errorMessage, status, createdAt, retriedAt);
    }

    private KafkaFailedMessage(String topic, String payload, String errorMessage, Status status,
            LocalDateTime createdAt, LocalDateTime retriedAt) {
        this.topic = topic;
        this.payload = payload;
        this.errorMessage = errorMessage;
        this.status = status;
        this.createdAt = createdAt;
        this.retriedAt = retriedAt;
    }
}
