package finpik.resolver.profile.application.impl;

import finpik.dto.RecommendLoanProductProfileDto;
import finpik.producer.RecommendLoanProductProducer;
import finpik.profile.entity.Profile;
import finpik.profile.service.ProfileService;
import finpik.resolver.profile.application.usecase.CreateProfileUseCase;
import finpik.resolver.profile.application.dto.CreateProfileUseCaseDto;
import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.user.entity.User;
import finpik.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateProfileUseCaseImpl implements CreateProfileUseCase {
    private final ProfileService profileService;
    private final UserService userService;
    private final RecommendLoanProductProducer recommendLoanProductProducer;

    @Override
    public ProfileDto execute(CreateProfileUseCaseDto dto) {
        User user = getUser(dto.userId());

        Profile profile = profileService.createProfile(dto.toDomainDto(user));

        RecommendLoanProductProfileDto kafkaDto = RecommendLoanProductProfileDto.builder().profileId(profile.getId())
            .creditScore(profile.getCreditScore()).purposeOfLoan(profile.getPurposeOfLoan())
            .occupation(profile.getOccupation()).build();

        recommendLoanProductProducer.sendMessageForRecommendLoanProduct(kafkaDto);

        return new ProfileDto(profile);
    }

    private User getUser(Long userId) {
        return userService.findUserBy(userId);
    }
}
