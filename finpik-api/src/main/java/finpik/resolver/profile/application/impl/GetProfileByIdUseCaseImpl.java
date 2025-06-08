package finpik.resolver.profile.application.impl;

import finpik.profile.entity.Profile;
import finpik.profile.service.ProfileService;
import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.resolver.profile.application.usecase.GetProfileByIdUseCase;
import finpik.user.entity.User;
import finpik.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetProfileByIdUseCaseImpl implements GetProfileByIdUseCase {
    private final ProfileService profileService;
    private final UserService userService;

    @Override
    public ProfileDto execute(Long id, Long userId) {
        User user = userService.findUserBy(userId);

        Profile profile = profileService.getProfileBy(id, user);

        return new ProfileDto(profile);
    }
}
