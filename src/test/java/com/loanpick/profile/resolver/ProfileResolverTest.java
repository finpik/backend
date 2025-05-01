package com.loanpick.profile.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;
import com.loanpick.profile.resolver.input.CreateProfileInput;
import com.loanpick.profile.resolver.result.ProfileResult;
import com.loanpick.profile.service.ProfileService;
import com.loanpick.user.entity.Gender;
import com.loanpick.user.entity.RegistrationType;
import com.loanpick.user.entity.User;

class ProfileResolverTest {
    private final ProfileService profileService = mock(ProfileService.class);
    private final ProfileResolver profileResolver = new ProfileResolver(profileService);

    @DisplayName("createProfile 호출 시 ProfileResult로 변환된다")
    @Test
    void createProfile() {
        // given
        CreateProfileInput input = CreateProfileInput.builder().employmentStatus(EmploymentStatus.EMPLOYEE)
                .workplaceName("ABC Corp").employmentForm(EmploymentForm.FULL_TIME).income(50000000)
                .employmentDate(LocalDate.of(2020, 1, 1)).purposeOfLoan(PurposeOfLoan.HOUSING)
                .desiredLoanAmount(5000000).loanProductUsageStatus(LoanProductUsageStatus.USING)
                .loanProductUsageCount(2).totalLoanUsageAmount(10000000).creditScore(800)
                .creditGradeStatus(CreditGradeStatus.UPPER).profileName("프로필1").build();

        Profile profile = Profile.builder().employmentStatus(input.employmentStatus())
                .workplaceName(input.workplaceName()).employmentForm(input.employmentForm()).income(input.income())
                .employmentDate(input.employmentDate()).purposeOfLoan(input.purposeOfLoan())
                .desiredLoanAmount(input.desiredLoanAmount()).loanProductUsageStatus(input.loanProductUsageStatus())
                .loanProductUsageCount(input.loanProductUsageCount()).totalLoanUsageAmount(input.totalLoanUsageAmount())
                .creditScore(input.creditScore()).creditGradeStatus(input.creditGradeStatus())
                .profileName(input.profileName()).build();

        User user = User.builder().id(1L).username("loanpick").email("loanpick@gmail.com").gender(Gender.MALE)
                .registrationType(RegistrationType.KAKAO).build();

        when(profileService.createProfile(input.toDto(user))).thenReturn(profile);

        // when
        ProfileResult result = profileResolver.createProfile(input, user);

        // then
        assertAll(() -> assertThat(result).isNotNull(),
                () -> assertThat(result.purposeOfLoan()).isEqualTo(profile.getPurposeOfLoan()),
                () -> assertThat(result.creditGradeStatus()).isEqualTo(profile.getCreditGradeStatus()),
                () -> assertThat(result.loanProductUsageCount()).isEqualTo(profile.getLoanProductUsageCount()),
                () -> assertThat(result.totalLoanUsageAmount()).isEqualTo(profile.getTotalLoanUsageAmount()),
                () -> assertThat(result.desiredLoanAmount()).isEqualTo(profile.getDesiredLoanAmount()),
                () -> assertThat(result.employmentStatus()).isEqualTo(profile.getEmploymentStatus()));
    }

    @Test
    @DisplayName("profileByUserId 호출 시 Profile 리스트가 ProfileResult 리스트로 변환된다")
    void profileByUserId() {
        // given
        User user = User.builder().id(1L).username("loanpick").email("loanpick@gmail.com").gender(Gender.MALE)
                .registrationType(RegistrationType.KAKAO).build();

        Profile profile1 = Profile.builder().profileName("프로필1").employmentStatus(EmploymentStatus.EMPLOYEE)
                .purposeOfLoan(PurposeOfLoan.HOUSING).creditGradeStatus(CreditGradeStatus.UPPER)
                .loanProductUsageCount(2).totalLoanUsageAmount(10000000).desiredLoanAmount(5000000).build();

        Profile profile2 = Profile.builder().profileName("프로필2").employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                .purposeOfLoan(PurposeOfLoan.LIVING_EXPENSES).creditGradeStatus(CreditGradeStatus.LOWER)
                .loanProductUsageCount(1).totalLoanUsageAmount(3000000).desiredLoanAmount(2000000).build();

        List<Profile> profileList = List.of(profile1, profile2);

        when(profileService.getAllProfiles(user)).thenReturn(profileList);

        // when
        List<ProfileResult> result = profileResolver.profileByUserId(user);

        // then
        assertAll(() -> assertThat(result).hasSize(2), () -> assertThat(result.get(0).profileName()).isEqualTo("프로필1"),
                () -> assertThat(result.get(1).profileName()).isEqualTo("프로필2"),
                () -> assertThat(result.get(0).purposeOfLoan()).isEqualTo(profile1.getPurposeOfLoan()),
                () -> assertThat(result.get(1).creditGradeStatus()).isEqualTo(profile2.getCreditGradeStatus()));
    }
}
