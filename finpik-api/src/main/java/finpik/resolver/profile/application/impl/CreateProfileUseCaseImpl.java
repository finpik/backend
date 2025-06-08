package finpik.resolver.profile.application.impl;

import finpik.dto.RecommendLoanProductProfileEvent;
import finpik.profile.entity.Profile;
import finpik.profile.entity.ProfileList;
import finpik.repository.profile.ProfileRepository;
import finpik.resolver.profile.application.usecase.CreateProfileUseCase;
import finpik.resolver.profile.application.dto.CreateProfileUseCaseDto;
import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.user.entity.User;
import finpik.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateProfileUseCaseImpl implements CreateProfileUseCase {
    private final UserService userService;
    private final ProfileRepository profileRepository;
    private final ApplicationEventPublisher eventPublisher;

    private static final int START_SEQ = 1;

    @Override
    public ProfileDto execute(CreateProfileUseCaseDto dto) {
        User user = getUser(dto.userId());

        Profile profile = dto.toDomain(user);

        ProfileList profileList = findProfileListBy(user);
        profileList.validateProfileCountLimit();

        profileList.balanceSequence(START_SEQ);

        sendEvent(profile);

        return new ProfileDto(profileRepository.save(profile));
    }

    private User getUser(Long userId) {
        return userService.findUserBy(userId);
    }


    private void sendEvent(Profile profile) {
        RecommendLoanProductProfileEvent event = RecommendLoanProductProfileEvent.builder().profileId(profile.getId())
            .creditScore(profile.getCreditScore()).purposeOfLoan(profile.getPurposeOfLoan())
            .occupation(profile.getOccupation()).build();

        eventPublisher.publishEvent(event);
    }

    private ProfileList findProfileListBy(User user) {
        return profileRepository.findByUser(user);
    }
}
