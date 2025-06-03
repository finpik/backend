package finpik.db.jpa.repository.history.dlq;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finpik.db.entity.history.dlq.KafkaFailedMessage;

@Repository
public interface KafkaFailedMessageJpaRepository extends JpaRepository<KafkaFailedMessage, Long> {
}
