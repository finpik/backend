package finpik.profile.repository;

import java.util.List;
import java.util.Optional;

import finpik.profile.entity.Profile;
import finpik.user.entity.User;

public interface ProfileRepository {
    Profile save(Profile profile);

    Profile update(Profile profile);

    List<Profile> updateAll(List<Profile> profiles);

    Optional<Profile> findById(Long id);

    List<Profile> findByUser(User user);

    List<Profile> findAllById(List<Long> ids);

    void deleteById(Long id);
}
