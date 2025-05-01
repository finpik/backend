package com.loanpick.profile.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("내 프로필").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).build();

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

    @DisplayName("새로운 프로필 생성시 기존의 프로필의 순번이 1개씩 밀리고, 새로운 프로필의 순번이 1번이 된다.")
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
    void getAllProfiles() {
        //given
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

        //when
        List<Profile> foundProfileList = profileService.getAllProfiles(savedUser);

        //then
        assertThat(2).isEqualTo(foundProfileList.size());
    }
}
