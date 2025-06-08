package finpik.resolver.profile.application.impl;

import finpik.profile.entity.Profile;
import finpik.profile.service.ProfileService;
import finpik.resolver.profile.application.usecase.UpdateProfileUseCase;
import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.resolver.profile.application.dto.UpdateProfileUseCaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateProfileUseCaseImpl implements UpdateProfileUseCase {
    private final ProfileService profileService;

    @Override
    public ProfileDto execute(UpdateProfileUseCaseDto dto) {
        Profile profile = profileService.updateProfile(dto.toDomainDto());

        return new ProfileDto(profile);
    }
}
