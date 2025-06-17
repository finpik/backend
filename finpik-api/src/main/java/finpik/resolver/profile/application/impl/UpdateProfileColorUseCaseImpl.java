package finpik.resolver.profile.application.impl;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.profile.entity.Profile;
import finpik.repository.profile.ProfileRepository;
import finpik.resolver.profile.application.usecase.UpdateProfileColorUseCase;
import finpik.resolver.profile.application.dto.ProfileResultDto;
import finpik.resolver.profile.application.dto.UpdateProfileColorUseCaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateProfileColorUseCaseImpl implements UpdateProfileColorUseCase {
    private final ProfileRepository profileRepository;

    @Override
    public ProfileResultDto execute(UpdateProfileColorUseCaseDto dto) {
        Profile profile = findProfileBy(dto.id());

        profile.changeProfileColor(dto.profileColor());

        return new ProfileResultDto(profileRepository.update(profile));
    }

    private Profile findProfileBy(long id) {
        return profileRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PROFILE));
    }
}
