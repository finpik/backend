package finpik.resolver.profile.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finpik.dto.RecommendLoanProductProfileDto;
import finpik.producer.RecommendLoanProductProducer;
import finpik.profile.entity.Profile;
import finpik.profile.service.ProfileService;
import finpik.profile.service.dto.UpdateProfileSequenceDto;
import finpik.resolver.profile.application.dto.CreateProfileUseCaseDto;
import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.resolver.profile.application.dto.UpdateProfileColorUseCaseDto;
import finpik.resolver.profile.application.dto.UpdateProfileSequenceUseCaseDto;
import finpik.resolver.profile.application.dto.UpdateProfileUseCaseDto;
import finpik.user.entity.User;
import finpik.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileCommandService {
    private final ProfileService profileService;
    private final UserService userService;
    private final RecommendLoanProductProducer recommendLoanProductProducer;

    @Transactional
    public ProfileDto createProfile(CreateProfileUseCaseDto dto) {
        User user = getUser(dto.userId());

        Profile profile = profileService.createProfile(dto.toDomainDto(user));

        RecommendLoanProductProfileDto kafkaDto = RecommendLoanProductProfileDto.builder().profileId(profile.getId())
                .creditScore(profile.getCreditScore()).purposeOfLoan(profile.getPurposeOfLoan())
                .occupation(profile.getOccupation()).build();

        recommendLoanProductProducer.sendMessageForRecommendLoanProduct(kafkaDto);

        return new ProfileDto(profile);
    }

    @Transactional
    public List<ProfileDto> deleteProfile(Long profileId, Long userId) {
        User user = getUser(userId);

        List<Profile> profileList = profileService.deleteProfile(profileId, user);

        return profileList.stream().map(ProfileDto::new).toList();
    }

    @Transactional
    public ProfileDto updateProfile(UpdateProfileUseCaseDto dto) {
        Profile profile = profileService.updateProfile(dto.toDomainDto());

        return new ProfileDto(profile);
    }

    @Transactional
    public List<ProfileDto> updateProfileSequence(List<UpdateProfileSequenceUseCaseDto> dtos, Long userId) {
        User user = getUser(userId);

        List<UpdateProfileSequenceDto> domainDtos = dtos.stream().map(UpdateProfileSequenceUseCaseDto::toDomainDto)
                .toList();

        List<Profile> profileList = profileService.updateProfileSequence(domainDtos, user);

        return profileList.stream().map(ProfileDto::new).toList();
    }

    @Transactional
    public ProfileDto updateProfileColor(UpdateProfileColorUseCaseDto dto) {
        Profile profile = profileService.updateProfileColor(dto.toDomainDto());

        return new ProfileDto(profile);
    }

    private User getUser(Long userId) {
        return userService.findUserBy(userId);
    }
}
