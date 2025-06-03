package finpik.jpa.repository.history.dlq;

import org.springframework.data.jpa.repository.JpaRepository;

import finpik.entity.history.dlq.KafkaFailedMessage;

public interface KafkaFailedMessageJpaRepository extends JpaRepository<KafkaFailedMessage, Long> {
}
