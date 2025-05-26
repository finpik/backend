package finpik.resolver.profile.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finpik.profile.entity.Profile;
import finpik.profile.service.ProfileService;
import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.user.entity.User;
import finpik.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileQueryService {
    private final ProfileService profileService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<ProfileDto> getProfileListByUser(Long userId) {
        User user = getUserById(userId);

        List<Profile> profileList = profileService.getProfileListBy(user);

        return profileList.stream().map(ProfileDto::new).toList();
    }

    @Transactional(readOnly = true)
    public ProfileDto getProfileById(Long id, Long userId) {
        User user = getUserById(userId);

        Profile profile = profileService.getProfileBy(id, user);

        return new ProfileDto(profile);
    }

    private User getUserById(Long userId) {
        return userService.findUserBy(userId);
    }
}
