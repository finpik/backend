package finpik.user.repository;

import java.util.Optional;

import finpik.user.entity.User;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
}
