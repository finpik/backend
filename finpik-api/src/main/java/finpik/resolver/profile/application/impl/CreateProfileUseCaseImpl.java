package finpik.resolver.profile.application.impl;

import finpik.dto.RecommendLoanProductProfileEvent;
import finpik.entity.Profile;
import finpik.entity.ProfileList;
import finpik.entity.User;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.repository.profile.ProfileRepository;
import finpik.repository.user.UserRepository;
import finpik.resolver.profile.application.usecase.CreateProfileUseCase;
import finpik.resolver.profile.application.dto.CreateProfileUseCaseDto;
import finpik.resolver.profile.application.dto.ProfileResultDto;
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

    @Override
    public ProfileResultDto execute(CreateProfileUseCaseDto dto) {
        User user = getUser(dto.userId());

        Profile profile = dto.toDomain(user);

        ProfileList profileList = findProfileListBy(user);

        ProfileList addedProfileList = profileList.addProfile(profile);

        Profile savedProfile = profileRepository.saveNewAndUpdateExistProfileList(addedProfileList);

        sendEvent(savedProfile);

        return new ProfileResultDto(savedProfile);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
    }


    private void sendEvent(Profile profile) {
        RecommendLoanProductProfileEvent event = RecommendLoanProductProfileEvent.builder()
            .profileId(profile.getId())
            .desiredLimit(profile.getDesiredLoanAmount())
            .creditScore(profile.getCreditScore().creditScore())
            .occupation(profile.getOccupationDetail().getOccupation())
            .employmentForm(profile.getOccupationDetail().getEmploymentForm())
            .gender(profile.getUser().getGender())
            .age(profile.getUser().getAge())
            .build();

        eventPublisher.publishEvent(event);
    }

    private ProfileList findProfileListBy(User user) {
        return profileRepository.findByUser(user);
    }
}
