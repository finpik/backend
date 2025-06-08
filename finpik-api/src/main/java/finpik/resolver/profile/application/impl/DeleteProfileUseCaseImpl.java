package finpik.resolver.profile.application.impl;

import finpik.profile.entity.Profile;
import finpik.profile.service.ProfileService;
import finpik.resolver.profile.application.usecase.DeleteProfileUseCase;
import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.user.entity.User;
import finpik.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteProfileUseCaseImpl implements DeleteProfileUseCase {
    private final ProfileService profileService;
    private final UserService userService;

    @Override
    public List<ProfileDto> execute(Long profileId, Long userId) {
        User user = userService.findUserBy(userId);

        List<Profile> profileList = profileService.deleteProfile(profileId, user);

        return profileList.stream().map(ProfileDto::new).toList();
    }
}
