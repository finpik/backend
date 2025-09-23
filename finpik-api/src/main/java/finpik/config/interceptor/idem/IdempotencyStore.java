package finpik.config.interceptor.idem;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface IdempotencyStore {
    Optional<IdemStatus> get(String idemKey);
}
