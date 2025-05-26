package finpik.db.jpa.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import finpik.db.entity.user.UserEntity;

public interface UserEntityJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
