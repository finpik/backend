package finpik.repository.profile;

import java.util.List;
import java.util.Optional;

import finpik.profile.entity.Profile;
import finpik.profile.entity.ProfileList;
import finpik.user.entity.User;

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
