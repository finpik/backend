package finpik.jpa.repository.profile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finpik.entity.profile.ProfileEntity;
import finpik.entity.user.UserEntity;

public interface ProfileEntityJpaRepository extends JpaRepository<ProfileEntity, Long> {
    List<ProfileEntity> findByUser(UserEntity userEntity);
}
