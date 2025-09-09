package finpik.resolver.profile.application.impl;

import finpik.entity.ProfileList;
import finpik.entity.User;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.repository.profile.ProfileRepository;
import finpik.resolver.profile.application.usecase.DeleteProfileUseCase;
import finpik.resolver.profile.application.dto.ProfileResultDto;
import finpik.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteProfileUseCaseImpl implements DeleteProfileUseCase {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public List<ProfileResultDto> execute(List<Long> deletedIdList, Long userId) {
        User user = findUserBy(userId);

        ProfileList foundProfileList = findProfileListBy(user);

        ProfileList profileList = foundProfileList.deleteProfileList(deletedIdList);

        profileRepository.deleteAllById(deletedIdList);

        return profileList.getProfileList().stream().map(ProfileResultDto::new).toList();
    }

    private ProfileList findProfileListBy(User user) {
        return profileRepository.findByUser(user);
    }

    private User findUserBy(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
    }
}
