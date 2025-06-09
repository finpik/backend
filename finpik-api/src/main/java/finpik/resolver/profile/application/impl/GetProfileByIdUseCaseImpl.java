package finpik.resolver.profile.application.impl;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.profile.entity.Profile;
import finpik.repository.profile.ProfileRepository;
import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.resolver.profile.application.usecase.GetProfileByIdUseCase;
import finpik.user.entity.User;
import finpik.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetProfileByIdUseCaseImpl implements GetProfileByIdUseCase {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public ProfileDto execute(Long id, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Profile profile = profileRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PROFILE));

        validateProfileOwner(profile, user);

        return new ProfileDto(profile);
    }

    private void validateProfileOwner(Profile profile, User user) {
        if (!profile.getUser().equals(user)) {
            throw new BusinessException(ErrorCode.NOT_PROFILE_OWNER);
        }
    }
}
