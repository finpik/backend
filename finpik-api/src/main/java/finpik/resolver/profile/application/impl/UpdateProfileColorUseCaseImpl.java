package finpik.resolver.profile.application.impl;

import finpik.profile.entity.Profile;
import finpik.profile.service.ProfileService;
import finpik.resolver.profile.application.usecase.UpdateProfileColorUseCase;
import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.resolver.profile.application.dto.UpdateProfileColorUseCaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateProfileColorUseCaseImpl implements UpdateProfileColorUseCase {
    private final ProfileService profileService;

    @Override
    public ProfileDto execute(UpdateProfileColorUseCaseDto dto) {
        Profile profile = profileService.updateProfileColor(dto.toDomainDto());

        return new ProfileDto(profile);
    }
}
