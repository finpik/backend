package finpik.resolver.profile.application.impl;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.profile.entity.ProfileList;
import finpik.repository.profile.ProfileRepository;
import finpik.resolver.profile.application.usecase.DeleteProfileUseCase;
import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.user.entity.User;
import finpik.user.repository.UserRepository;
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

    private static final int START_SEQ = 0;

    @Override
    public List<ProfileDto> execute(Long deletedId, Long userId) {
        User user = findUserBy(userId);

        ProfileList foundProfileList = findProfileListBy(user);

        profileRepository.deleteById(deletedId);

        ProfileList profileList = foundProfileList.deleteProfile(deletedId);

        profileList.balanceSequence(START_SEQ);

        return profileList.getProfiles().stream().map(ProfileDto::new).toList();
    }

    private ProfileList findProfileListBy(User user) {
        return profileRepository.findByUser(user);
    }

    private User findUserBy(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
    }
}
