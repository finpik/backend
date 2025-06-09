package finpik.resolver.profile.application.impl;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.profile.entity.ProfileList;
import finpik.repository.profile.ProfileRepository;
import finpik.repository.user.UserRepository;
import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.resolver.profile.application.usecase.GetProfileListByUserUseCase;
import finpik.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetProfileListByUserUseCaseImpl implements GetProfileListByUserUseCase {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public List<ProfileDto> execute(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        ProfileList profileList = profileRepository.findByUser(user);

        return profileList.getProfiles().stream().map(ProfileDto::new).toList();
    }
}
