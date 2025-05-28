package finpik.profile.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.error.enums.ErrorCode;
import finpik.profile.entity.Profile;
import finpik.profile.repository.ProfileRepository;
import finpik.profile.service.dto.CreateProfileDto;
import finpik.profile.service.dto.UpdateProfileColorDto;
import finpik.profile.service.dto.UpdateProfileDto;
import finpik.profile.service.dto.UpdateProfileSequenceDto;
import finpik.user.entity.User;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Mock
    private ProfileRepository profileRepository;

    @DisplayName("프로 파일을 저장하면 저장된 프로파일이 리턴되고 생성된 객체의 순번이 0번이 된다.")
    @Test
    void createProfile() {
        // given
        User user = User.builder().id(1L).email("test@email.com").build();

        CreateProfileDto dto = getCreateProfileDto(user);

        Profile profile = Profile.builder().id(1L).seq(0).creditScore(750).build();

        when(profileRepository.save(any())).thenReturn(profile);

        // when
        Profile result = profileService.createProfile(dto);

        // then
        assertAll(() -> assertThat(result).isEqualTo(profile),
                () -> assertThat(result.getSeq()).isEqualTo(profile.getSeq()));
    }

    @DisplayName("기존에 존재하던 프로필이 있을 경우 새로 생성된 프로필이 1번이 되고 나머지 값들은 순번이 밀린다.")
    @Test
    void createProfileBalanceSequence() {
        // given
        User user = User.builder().id(1L).email("test@email.com").build();

        CreateProfileDto dto = getCreateProfileDto(user);

        Profile firstExistingProfile = Profile.builder().id(1L).seq(0).creditScore(750).build();

        Profile secondExistingProfile = Profile.builder().id(2L).seq(1).creditScore(750).build();

        Profile newProfile = Profile.builder().id(3L).seq(0).creditScore(750).build();

        when(profileRepository.findByUser(user)).thenReturn(List.of(firstExistingProfile, secondExistingProfile));
        when(profileRepository.save(any())).thenReturn(newProfile);

        // when
        profileService.createProfile(dto);

        // then
        assertAll(() -> assertThat(firstExistingProfile.getSeq()).isEqualTo(1),
                () -> assertThat(secondExistingProfile.getSeq()).isEqualTo(2));
    }

    @DisplayName("이미 4개의 프로필이 존재할 경우 더 이상 만들 수 없다. EXCEEDING_PROFILE_COUNT_LIMIT")
    @Test
    void validateProfileCountLimit() {
        // given
        User user = User.builder().id(1L).email("test@email.com").build();

        CreateProfileDto dto = getCreateProfileDto(user);

        Profile firstExistingProfile = Profile.builder().id(1L).seq(0).creditScore(750).build();

        Profile secondExistingProfile = Profile.builder().id(2L).seq(1).creditScore(750).build();

        Profile thirdExistingProfile = Profile.builder().id(1L).seq(2).creditScore(750).build();

        Profile fourthExistingProfile = Profile.builder().id(2L).seq(3).creditScore(750).build();

        when(profileRepository.findByUser(user)).thenReturn(
                List.of(firstExistingProfile, secondExistingProfile, thirdExistingProfile, fourthExistingProfile));

        // when
        // then
        assertThatThrownBy(() -> profileService.createProfile(dto))
                .hasMessage(ErrorCode.EXCEEDING_PROFILE_COUNT_LIMIT.getMessage());
    }

    private CreateProfileDto getCreateProfileDto(User user) {
        return CreateProfileDto.builder().employmentForm(EmploymentForm.FULL_TIME).occupation(Occupation.SELF_EMPLOYED)
                .loanProductUsageStatus(LoanProductUsageStatus.NOT_USING).loanProductUsageCount(0)
                .totalLoanUsageAmount(0).purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(5000000)
                .creditGradeStatus(CreditGradeStatus.EXCELLENT).creditScore(750).profileName("마이 프로필")
                .profileColor(ProfileColor.GRAY_TWO).user(user).build();
    }

    @DisplayName("유저가 가지고 있는 프로필을 조회할 수 있다.")
    @Test
    void getProfileListBy() {
        // given
        User user = User.builder().id(1L).email("test@email.com").build();

        Profile firstExistingProfile = Profile.builder().id(1L).seq(0).creditScore(750).build();

        Profile secondExistingProfile = Profile.builder().id(2L).seq(1).creditScore(750).build();

        when(profileRepository.findByUser(user)).thenReturn(List.of(firstExistingProfile, secondExistingProfile));

        // when
        List<Profile> results = profileService.getProfileListBy(user);

        // then
        assertAll(() -> assertThat(results.size()).isEqualTo(2),
                () -> assertThat(results.getFirst().getId()).isEqualTo(1L),
                () -> assertThat(results.get(1).getId()).isEqualTo(2L));
    }

    @DisplayName("특정 프로필을 업데이트 할 수 있다.")
    @Test
    void updateProfile() {
        // given
        UpdateProfileDto dto = UpdateProfileDto.builder().id(1L).occupation(Occupation.SELF_EMPLOYED)
                .employmentForm(EmploymentForm.FULL_TIME).purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT)
                .desiredLoanAmount(10000000).loanProductUsageStatus(LoanProductUsageStatus.NOT_USING)
                .loanProductUsageCount(0).totalLoanUsageAmount(0).creditScore(760)
                .creditGradeStatus(CreditGradeStatus.GOOD).profileName("내 프로필").build();

        Profile profile = getDefaultProfile(User.builder().build());

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.update(profile)).thenReturn(profile);

        // when
        profileService.updateProfile(dto);

        // then
        assertAll(() -> assertThat(profile.getOccupation()).isEqualTo(Occupation.SELF_EMPLOYED),
                () -> assertThat(profile.getEmploymentForm()).isEqualTo(EmploymentForm.FULL_TIME),
                () -> assertThat(profile.getCreditScore()).isEqualTo(760),
                () -> assertThat(profile.getCreditGradeStatus()).isEqualTo(CreditGradeStatus.GOOD));
    }

    @DisplayName("프로필 색상을 변경할 수 있다.")
    @Test
    void updateProfileColor() {
        // given
        UpdateProfileColorDto dto = UpdateProfileColorDto.builder().id(1L).profileColor(ProfileColor.GRAY_TWO).build();

        Profile profile = getDefaultProfile(User.builder().build());

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(profile)).thenReturn(profile);

        // when
        profileService.updateProfileColor(dto);

        // then
        assertThat(profile.getProfileColor()).isEqualTo(ProfileColor.GRAY_TWO);
    }

    @DisplayName("프로필의 순번을 조정할 수 있다.")
    @Test
    void updateProfileSequence() {
        // given
        Long firstId = 1L;
        Long secondId = 2L;

        User user = User.builder().id(1L).email("test@email.com").build();

        UpdateProfileSequenceDto firstDto = UpdateProfileSequenceDto.builder().id(firstId).seq(1).build();

        UpdateProfileSequenceDto secondDto = UpdateProfileSequenceDto.builder().id(secondId).seq(0).build();

        Profile firstProfile = getSeqProfile(firstId, 0, user);
        Profile secondProfile = getSeqProfile(secondId, 1, user);

        when(profileRepository.findAllById(List.of(firstId, secondId)))
                .thenReturn(List.of(firstProfile, secondProfile));

        when(profileRepository.updateAll(List.of(firstProfile, secondProfile)))
                .thenReturn(List.of(firstProfile, secondProfile));

        // when
        List<Profile> results = profileService.updateProfileSequence(List.of(firstDto, secondDto), user);

        // then
        assertAll(() -> assertThat(results.size()).isEqualTo(2),
                () -> assertThat(results.getFirst().getSeq()).isEqualTo(1),
                () -> assertThat(results.get(1).getSeq()).isEqualTo(0));
    }

    @DisplayName("중복된 순번이 존재할 경우 예외가 발생한다. DUPLICATE_PROFILE_SEQ")
    @Test
    void validateDuplicateSequence() {
        // given
        Long firstId = 1L;
        Long secondId = 2L;

        User user = User.builder().id(1L).email("test@email.com").build();

        UpdateProfileSequenceDto firstDto = UpdateProfileSequenceDto.builder().id(firstId).seq(0).build();

        UpdateProfileSequenceDto secondDto = UpdateProfileSequenceDto.builder().id(secondId).seq(0).build();

        // when
        // then
        assertThatThrownBy(() -> profileService.updateProfileSequence(List.of(firstDto, secondDto), user))
                .hasMessage(ErrorCode.DUPLICATE_PROFILE_SEQ.getMessage());
    }

    @DisplayName("소유가 아닌 프로필을 조회 했을 경우 예외가 발생한다. NOT_PROFILE_OWNER")
    @Test
    void validateProfileOwner() {
        // given
        Long firstId = 1L;

        User requester = User.builder().id(1L).email("request@email.com").build();
        User foundUser = User.builder().id(2L).email("realUser@email.com").build();

        UpdateProfileSequenceDto firstDto = UpdateProfileSequenceDto.builder().id(firstId).seq(0).build();

        Profile firstProfile = getSeqProfile(firstId, 0, foundUser);

        when(profileRepository.findAllById(List.of(firstId))).thenReturn(List.of(firstProfile));

        // when
        // then
        assertThatThrownBy(() -> profileService.updateProfileSequence(List.of(firstDto), requester))
                .hasMessage(ErrorCode.NOT_PROFILE_OWNER.getMessage());
    }

    @DisplayName("프로필을 제거하면 해당 프로필은 지워지고 지워진 순번이 재배치 된다.")
    @Test
    void deleteProfile() {
        // given
        User user = User.builder().id(1L).email("test@email.com").build();

        Profile firstProfile = getSeqProfile(1L, 0, user);
        Profile secondProfile = getSeqProfile(2L, 1, user);
        Profile thirdProfile = getSeqProfile(3L, 2, user);
        Profile deltingProfile = getSeqProfile(4L, 3, user);

        when(profileRepository.findByUser(user))
                .thenReturn(List.of(firstProfile, secondProfile, thirdProfile, deltingProfile));

        // when
        List<Profile> results = profileService.deleteProfile(deltingProfile.getId(), user);

        // then
        assertAll(() -> assertThat(results.size()).isEqualTo(3),
                () -> assertThat(results.getFirst().getId()).isEqualTo(1L),
                () -> assertThat(results.getFirst().getSeq()).isEqualTo(0),
                () -> assertThat(results.get(1).getId()).isEqualTo(2L),
                () -> assertThat(results.get(1).getSeq()).isEqualTo(1),
                () -> assertThat(results.getLast().getId()).isEqualTo(3L),
                () -> assertThat(results.getLast().getSeq()).isEqualTo(2));
    }

    @DisplayName("프로필을 한개 조회할 수 있다.")
    @Test
    void getProfileBy() {
        // given
        Long profileId = 1L;
        User user = User.builder().id(1L).email("test@email.com").build();

        Profile profile = getDefaultProfile(user);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

        // when
        Profile result = profileService.getProfileBy(profileId, user);

        // then
        assertThat(result).isEqualTo(profile);
    }

    private Profile getSeqProfile(Long id, Integer seq, User user) {
        return Profile.builder().id(id).occupation(Occupation.OTHER).employmentForm(EmploymentForm.CONTRACT)
                .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(10000000)
                .loanProductUsageStatus(LoanProductUsageStatus.NOT_USING).loanProductUsageCount(0)
                .totalLoanUsageAmount(0).creditScore(900).creditGradeStatus(CreditGradeStatus.EXCELLENT)
                .profileColor(ProfileColor.BLUE_TWO).profileName("내 전 프로필").seq(seq).user(user).build();
    }

    private Profile getDefaultProfile(User user) {
        return Profile.builder().id(1L).occupation(Occupation.OTHER).employmentForm(EmploymentForm.CONTRACT)
                .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(10000000)
                .loanProductUsageStatus(LoanProductUsageStatus.NOT_USING).loanProductUsageCount(0)
                .totalLoanUsageAmount(0).creditScore(900).creditGradeStatus(CreditGradeStatus.EXCELLENT)
                .profileColor(ProfileColor.BLUE_TWO).profileName("내 전 프로필").seq(0).user(user).build();
    }
}
