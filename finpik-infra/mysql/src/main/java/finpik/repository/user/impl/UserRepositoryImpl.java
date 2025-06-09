package finpik.repository.user.impl;

import java.util.Optional;

import finpik.jpa.repository.user.UserEntityJpaRepository;
import org.springframework.stereotype.Repository;

import finpik.entity.user.UserEntity;
import finpik.user.entity.User;
import finpik.repository.user.UserRepository;
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
