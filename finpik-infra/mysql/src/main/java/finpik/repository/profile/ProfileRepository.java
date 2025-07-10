package finpik.repository.profile;

import finpik.entity.Profile;
import finpik.entity.ProfileList;
import finpik.entity.User;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository {
    Profile save(Profile profile);

    Profile saveNewAndUpdateExistProfileList(ProfileList profileList);

    Profile update(Profile profile);

    ProfileList updateAll(ProfileList profileList);

    Optional<Profile> findById(Long id);

    ProfileList findByUser(User user);

    ProfileList findAllById(List<Long> ids);

    void deleteById(Long id);
}
