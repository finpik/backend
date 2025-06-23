package finpik.resolver.profile.application.impl;

import finpik.dto.RecommendLoanProductProfileEvent;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.profile.entity.Profile;
import finpik.profile.entity.ProfileList;
import finpik.repository.profile.ProfileRepository;
import finpik.repository.user.UserRepository;
import finpik.resolver.profile.application.usecase.CreateProfileUseCase;
import finpik.resolver.profile.application.dto.CreateProfileUseCaseDto;
import finpik.resolver.profile.application.dto.ProfileResultDto;
import finpik.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateProfileUseCaseImpl implements CreateProfileUseCase {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ApplicationEventPublisher eventPublisher;

    private static final int START_SEQ = 1;

    @Override
    public ProfileResultDto execute(CreateProfileUseCaseDto dto) {
        User user = getUser(dto.userId());

        Profile profile = dto.toDomain(user);

        ProfileList profileList = findProfileListBy(user);
        profileList.validateProfileCountLimit();

        profileList.balanceSequence(START_SEQ);

        sendEvent(profile);

        return new ProfileResultDto(profileRepository.save(profile));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
    }


    private void sendEvent(Profile profile) {
        RecommendLoanProductProfileEvent event = RecommendLoanProductProfileEvent.builder().profileId(profile.getId())
            .creditScore(profile.getCreditScore().creditScore())
            .occupation(profile.getOccupationDetail().getOccupation())
            .employmentForm(profile.getOccupationDetail().getEmploymentForm())
            .build();

        eventPublisher.publishEvent(event);
    }

    private ProfileList findProfileListBy(User user) {
        return profileRepository.findByUser(user);
    }
}
