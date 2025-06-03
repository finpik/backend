package finpik.kafka.producer.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import finpik.producer.dlq.KafkaReliableAsyncProducerService;

@ExtendWith(MockitoExtension.class)
class KafkaReliableAsyncProducerServiceTest {
    @Mock
    KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    ScheduledExecutorService retryExecutor;

    @InjectMocks
    KafkaReliableAsyncProducerService producerService;

    @TempDir
    Path tempDir;

    @DisplayName("3번의 시도가 넘어가면 dlq 메세지를 로그파일에 올려두면서 카프카 dlq 토픽에 메세지를 보낸다.")
    @Test
    void producerDLQTest() {
        // given
        String topic = "test-topic";
        String message = "test-message";
        int maxAttempts = 3;

        String tempFilePath = tempDir.resolve("dlq.log").toAbsolutePath().toString();
        ReflectionTestUtils.setField(producerService, "dlqLogFilePath", tempFilePath);

        when(retryExecutor.schedule(any(Runnable.class), anyLong(), any())).thenAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run();
            return null;
        });

        when(kafkaTemplate.send(anyString(), anyString()))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Kafka send fail")));

        when(kafkaTemplate.send(contains("-dlq"), anyString()))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("DLQ send fail")));

        // when
        producerService.sendAsyncWithRetryAndDlq(topic, message, maxAttempts);

        verify(retryExecutor, times(2)).schedule(any(Runnable.class), anyLong(), any());

        await().atMost(Duration.ofSeconds(3)).untilAsserted(() -> {
            File logFile = new File(tempDir.resolve("dlq.log").toUri());
            assertThat(logFile).exists();
            assertThat(Files.readString(logFile.toPath())).contains("test-message");
        });
    }
}
