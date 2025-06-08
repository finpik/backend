package finpik.resolver.profile.application.impl;

import finpik.profile.entity.Profile;
import finpik.profile.service.ProfileService;
import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.resolver.profile.application.usecase.GetProfileListByUserUseCase;
import finpik.user.entity.User;
import finpik.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetProfileListByUserUseCaseImpl implements GetProfileListByUserUseCase {
    private final ProfileService profileService;
    private final UserService userService;

    @Override
    public List<ProfileDto> execute(Long userId) {
        User user = userService.findUserBy(userId);

        List<Profile> profileList = profileService.getProfileListBy(user);

        return profileList.stream().map(ProfileDto::new).toList();
    }
}
