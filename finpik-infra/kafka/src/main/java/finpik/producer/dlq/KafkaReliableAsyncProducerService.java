package finpik.producer.dlq;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaReliableAsyncProducerService {
    @Value("${fin-pik.kafka.dlqLogFileName}")
    private String dlqLogFilePath;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ScheduledExecutorService retryExecutor;
    private static final int START_ATTEMPTS = 1;
    private static final int MAX_BACKOFF_MILLIS = 30_000;
    private static final String DLQ_TOPIC_SUFFIX = "-dlq";

    public void sendAsyncWithRetryAndDlq(String topic, String message, int maxAttempts) {
        doSend(topic, message, START_ATTEMPTS, maxAttempts);
    }

    private void doSend(String topic, String message, int attempt, int maxAttempts) {
        kafkaTemplate.send(topic, message).thenAccept(result -> handleSuccess(topic, message, result))
                .exceptionally(ex -> {
                    handleFailure(topic, message, attempt, maxAttempts, ex);
                    return null;
                });
    }

    private void handleSuccess(String topic, String message, SendResult<String, String> result) {
        log.info("Kafka 전송 성공 [topic={}, offset={}, message={}]", topic, result.getRecordMetadata().offset(), message);
    }

    private void handleFailure(String topic, String message, int attempt, int maxAttempts, Throwable ex) {
        log.warn("Kafka 전송 실패 [attempt={}, message={}]", attempt, message, ex);

        if (attempt < maxAttempts) {
            int backoff = calculateExponentialBackoff(attempt);
            log.info("재시도 예약 [nextAttempt={}, backoff={}ms]", attempt + 1, backoff);

            retryExecutor.schedule(() -> doSend(topic, message, attempt + 1, maxAttempts), backoff,
                    TimeUnit.MILLISECONDS);
        } else {
            log.error("최대 재시도 초과 → DLQ 전송 시도");
            sendToDlq(topic, message);
        }
    }

    private int calculateExponentialBackoff(int attempt) {
        return Math.min((1 << (attempt - 1)) * 1000, MAX_BACKOFF_MILLIS);
    }

    private void sendToDlq(String originalTopic, String message) {
        String dlqTopic = getDlqTopic(originalTopic);

        kafkaTemplate.send(dlqTopic, message).thenAccept(
                result -> log.warn("DLQ 전송 성공 [dlqTopic={}, offset={}]", dlqTopic, result.getRecordMetadata().offset()))
                .exceptionally(dlqEx -> {
                    log.error("DLQ 전송 실패 → 로컬 백업 수행", dlqEx);

                    backupMessageLocally(originalTopic, message);
                    return null;
                });
    }

    private String getDlqTopic(String originalTopic) {
        return originalTopic + DLQ_TOPIC_SUFFIX;
    }

    private void backupMessageLocally(String topic, String message) {
        LocalDateTime now = LocalDateTime.now();

        try (PrintWriter out = new PrintWriter(new FileWriter(dlqLogFilePath, true))) {
            out.printf("[%s] Topic: %s, Message: %s%n", now, topic, message);

            log.info("로컬 백업 성공");
        } catch (Exception e) {
            log.error("로컬 백업 실패 [{}] [message={}]", now, message, e);
        }
    }

    @PreDestroy
    public void shutdownExecutor() {
        log.info("Kafka RetryExecutor 종료 시작...");
        retryExecutor.shutdown();
    }
}
