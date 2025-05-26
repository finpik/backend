package finpik.db.jpa.repository.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import finpik.db.entity.user.UserEntity;
import finpik.user.entity.User;
import finpik.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserEntityJpaRepository userEntityJpaRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = UserEntity.from(user);

        return userEntityJpaRepository.save(userEntity).toDomain();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userEntityJpaRepository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userEntityJpaRepository.findByEmail(email).map(UserEntity::toDomain);
    }
}
