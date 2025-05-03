package com.loanpick.profile.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loanpick.error.enums.ErrorCode;
import com.loanpick.error.exception.BusinessException;
import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;
import com.loanpick.profile.repository.ProfileRepository;
import com.loanpick.profile.service.dto.CreateProfileDto;
import com.loanpick.profile.service.dto.UpdateProfileDto;
import com.loanpick.profile.service.dto.UpdateProfileSequenceDto;
import com.loanpick.user.entity.Gender;
import com.loanpick.user.entity.RegistrationType;
import com.loanpick.user.entity.User;
import com.loanpick.user.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
class ProfileServiceImplTest {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        profileRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("createProfile 호출 시 Profile이 저장된다")
    @Test
    void createProfile_success() {
        // given
        CreateProfileDto dto = CreateProfileDto.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).profileName("내 프로필")
                .employmentForm(EmploymentForm.FULL_TIME).loanProductUsageStatus(LoanProductUsageStatus.USING)
                .purposeOfLoan(PurposeOfLoan.HOUSING).employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                .creditGradeStatus(CreditGradeStatus.UPPER).build();

        // when
        Profile savedProfile = profileService.createProfile(dto);

        // then
        assertAll(() -> assertThat(savedProfile.getId()).isNotNull(),
                () -> assertThat(savedProfile.getProfileName()).isEqualTo(dto.profileName()),
                () -> assertThat(savedProfile.getDesiredLoanAmount()).isEqualTo(dto.desiredLoanAmount()),
                () -> assertThat(savedProfile.getLoanProductUsageCount()).isEqualTo(dto.loanProductUsageCount()),
                () -> assertThat(savedProfile.getTotalLoanUsageAmount()).isEqualTo(dto.totalLoanUsageAmount()),
                () -> assertThat(savedProfile.getCreditScore()).isEqualTo(dto.creditScore()),
                () -> assertThat(savedProfile.getIncome()).isEqualTo(dto.income()),
                () -> assertThat(savedProfile.getWorkplaceName()).isEqualTo(dto.workplaceName()),
                () -> assertThat(savedProfile.getEmploymentForm()).isEqualTo(dto.employmentForm()),
                () -> assertThat(savedProfile.getLoanProductUsageStatus()).isEqualTo(dto.loanProductUsageStatus()),
                () -> assertThat(savedProfile.getPurposeOfLoan()).isEqualTo(dto.purposeOfLoan()),
                () -> assertThat(savedProfile.getEmploymentStatus()).isEqualTo(dto.employmentStatus()),
                () -> assertThat(savedProfile.getCreditGradeStatus()).isEqualTo(dto.creditGradeStatus()),
                () -> assertThat(savedProfile.getEmploymentDate()).isEqualTo(dto.employmentDate()));
    }

    @DisplayName("새로운 프로필 생성시 기존의 프로필의 순번이 변하고, 새로운 프로필의 순번이 0번이 된다.")
    @Test
    void balanceProfileSequence() {
        // given
        User user = User.builder().username("findpick").email("finpick@gmail.com").gender(Gender.MALE)
                .registrationType(RegistrationType.KAKAO).build();

        User savedUser = userRepository.save(user);

        Profile profileFirst = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("프로필1").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(savedUser).seq(0).build();

        Profile profileSecond = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("프로필2").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(savedUser).seq(1).build();

        CreateProfileDto dto = CreateProfileDto.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("새프로필").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(user).build();

        List<Profile> profileList = List.of(profileFirst, profileSecond);
        profileRepository.saveAll(profileList);

        // when
        profileService.createProfile(dto);
        Map<String, Integer> savedProfileMap = profileRepository.findByUser(savedUser).stream()
                .collect(Collectors.toMap(Profile::getProfileName, Profile::getSeq));

        // then
        assertAll(() -> assertThat(0).isEqualTo(savedProfileMap.get("새프로필")),
                () -> assertThat(1).isEqualTo(savedProfileMap.get("프로필1")),
                () -> assertThat(2).isEqualTo(savedProfileMap.get("프로필2")));
    }

    @DisplayName("이미 프로필의 개수 제한에 가득찼다면 더 이상 만들 수 없다.")
    @Test
    void validateProfileCountLimit() {
        // given
        User user = User.builder().username("findpick").email("finpick@gmail.com").gender(Gender.MALE)
                .registrationType(RegistrationType.KAKAO).build();

        User savedUser = userRepository.save(user);

        Profile profileFirst = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("프로필1").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(savedUser).seq(0).build();

        Profile profileSecond = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("프로필2").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(savedUser).seq(1).build();

        Profile profileThird = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("프로필3").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(savedUser).seq(2).build();

        Profile profileFourth = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("프로필4").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(savedUser).seq(3).build();

        CreateProfileDto dto = CreateProfileDto.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("새프로필").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(user).build();

        List<Profile> profileList = List.of(profileFirst, profileSecond, profileThird, profileFourth);
        profileRepository.saveAll(profileList);

        // when
        // then
        assertThatThrownBy(() -> profileService.createProfile(dto)).isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.EXCEEDING_PROFILE_COUNT_LIMIT.getMessage());
    }

    @DisplayName("유저로 조회하면 관련된 프로필이 조회된다.")
    @Test
    void getProfileListBy() {
        // given
        User user = User.builder().username("findpick").email("finpick@gmail.com").gender(Gender.MALE)
                .registrationType(RegistrationType.KAKAO).build();

        User savedUser = userRepository.save(user);

        Profile profileFirst = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("프로필1").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(savedUser).seq(0).build();

        Profile profileSecond = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("프로필2").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(savedUser).seq(1).build();

        List<Profile> profileList = List.of(profileFirst, profileSecond);
        profileRepository.saveAll(profileList);

        // when
        List<Profile> foundProfileList = profileService.getProfileListBy(savedUser);

        // then
        assertThat(2).isEqualTo(foundProfileList.size());
    }

    @DisplayName("프로필을 수정할 수 있다.")
    @Test
    void updateProfile() {
        // given
        User user = User.builder().username("findpick").email("finpick@gmail.com").gender(Gender.MALE)
                .registrationType(RegistrationType.KAKAO).build();

        User savedUser = userRepository.save(user);

        Profile profile = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("프로필1").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(savedUser).seq(0).build();

        Profile savedProfile = profileRepository.save(profile);

        UpdateProfileDto dto = UpdateProfileDto.builder().id(savedProfile.getId())
                .employmentStatus(EmploymentStatus.EMPLOYEE).workplaceName("테스트 회사")
                .employmentForm(EmploymentForm.FULL_TIME).income(50000000).employmentDate(LocalDate.of(2020, 1, 1))
                .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(10000000)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).loanProductUsageCount(2)
                .totalLoanUsageAmount(20000000).creditScore(800).creditGradeStatus(CreditGradeStatus.UPPER)
                .profileName("바뀐 프로필").build();

        // when
        Profile chnagedProfile = profileService.updateProfile(dto);

        // then
        assertAll(() -> assertThat("바뀐 프로필").isEqualTo(chnagedProfile.getProfileName()),
                () -> assertThat(PurposeOfLoan.LOAN_REPAYMENT).isEqualTo(chnagedProfile.getPurposeOfLoan()));

    }

    @DisplayName("기존의 프로필의 순번을 바꿀 수 있다.")
    @Test
    void updateProfileSequence() {
        // given
        User user = User.builder().username("findpick").email("finpick@gmail.com").gender(Gender.MALE)
                .registrationType(RegistrationType.KAKAO).build();

        User savedUser = userRepository.save(user);

        Profile profileFirst = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("프로필1").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(savedUser).seq(0).build();

        Profile profileSecond = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("프로필2").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(savedUser).seq(1).build();

        List<Profile> profileList = List.of(profileFirst, profileSecond);
        List<Profile> savedProfileList = profileRepository.saveAll(profileList).stream()
                .sorted(Comparator.comparing(Profile::getId)).toList();

        List<UpdateProfileSequenceDto> dto = List.of(
                UpdateProfileSequenceDto.builder().id(savedProfileList.get(0).getId()).seq(1).build(),
                UpdateProfileSequenceDto.builder().id(savedProfileList.get(1).getId()).seq(0).build());

        // when
        List<Profile> changedProfileList = profileService.updateProfileSequence(dto, user).stream()
                .sorted(Comparator.comparing(Profile::getId)).toList();

        // then
        assertAll(() -> assertThat(1L).isEqualTo(changedProfileList.get(0).getId()),
                () -> assertThat(1).isEqualTo(changedProfileList.get(0).getSeq()),
                () -> assertThat(2L).isEqualTo(changedProfileList.get(1).getId()),
                () -> assertThat(0).isEqualTo(changedProfileList.get(1).getSeq()));
    }

    @DisplayName("프로필 업데이트시 아이디 혹은 순번이 중복되면 에러가 발생한다.")
    @ParameterizedTest
    @MethodSource("provideUpdateProfileSequenceDto")
    void validateDuplicate(List<UpdateProfileSequenceDto> dtos, String expectedMessage) {
        // given
        User user = User.builder().username("findpick").email("finpick@gmail.com").gender(Gender.MALE)
                .registrationType(RegistrationType.KAKAO).build();

        User savedUser = userRepository.save(user);

        Profile profileFirst = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("프로필1").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(savedUser).seq(0).build();

        Profile profileSecond = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("프로필2").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).user(savedUser).seq(1).build();

        List<Profile> profileList = List.of(profileFirst, profileSecond);
        profileRepository.saveAll(profileList);

        // when
        // then
        assertThatThrownBy(() -> profileService.updateProfileSequence(dtos, user)).hasMessage(expectedMessage);
    }

    private static Stream<Arguments> provideUpdateProfileSequenceDto() {
        UpdateProfileSequenceDto dtoDuplicateIdFirst = updateProfileSequenceDtoBuilder().seq(0).build();
        UpdateProfileSequenceDto dtoDuplicateIdSecond = updateProfileSequenceDtoBuilder().seq(1).build();
        List<UpdateProfileSequenceDto> dtoDuplicateIdList = List.of(dtoDuplicateIdFirst, dtoDuplicateIdSecond);

        UpdateProfileSequenceDto dtoDuplicateSeqFirst = updateProfileSequenceDtoBuilder().id(1L).build();
        UpdateProfileSequenceDto dtoDuplicateSeqSecond = updateProfileSequenceDtoBuilder().id(2L).build();
        List<UpdateProfileSequenceDto> duplicateSeqList = List.of(dtoDuplicateSeqFirst, dtoDuplicateSeqSecond);

        return Stream.of(arguments(dtoDuplicateIdList, "프로필을 중복해서 업데이트 할 수 없습니다."),
                arguments(duplicateSeqList, "프로필 순번이 중복이 될 수 없습니다."));
    }

    private static UpdateProfileSequenceDto.UpdateProfileSequenceDtoBuilder updateProfileSequenceDtoBuilder() {
        return UpdateProfileSequenceDto.builder().id(1L).seq(0);
    }
}
