package finpik.resolver.profile.application.impl;

import finpik.entity.ProfileList;
import finpik.entity.User;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.repository.profile.ProfileRepository;
import finpik.repository.user.UserRepository;
import finpik.resolver.profile.application.dto.ProfileResultDto;
import finpik.resolver.profile.application.usecase.GetProfileListByUserUseCase;
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
    public List<ProfileResultDto> execute(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        ProfileList profileList = profileRepository.findByUser(user);

        return profileList.getProfileList().stream().map(ProfileResultDto::new).toList();
    }
}
