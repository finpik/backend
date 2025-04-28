package com.loanpick.profile.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;
import com.loanpick.profile.resolver.input.CreateProfileInput;
import com.loanpick.profile.resolver.result.ProfileResult;
import com.loanpick.profile.service.ProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ProfileResolverTest {
    private final ProfileService profileService = mock(ProfileService.class);
    private final ProfileResolver profileResolver = new ProfileResolver(profileService);

    @Test
    @DisplayName("createProfile 호출 시 ProfileResult로 변환된다")
    void createProfile() {
        // given
        CreateProfileInput input = CreateProfileInput.builder()
            .employmentStatus(EmploymentStatus.EMPLOYEE)
            .workplaceName("ABC Corp")
            .employmentForm(EmploymentForm.FULL_TIME)
            .income(50000000)
            .employmentDate(LocalDate.of(2020, 1, 1))
            .purposeOfLoan(PurposeOfLoan.HOUSING)
            .desiredLoanAmount(5000000)
            .loanProductUsageStatus(LoanProductUsageStatus.USING)
            .loanProductUsageCount(2)
            .totalLoanUsageAmount(10000000)
            .creditScore(800)
            .creditGradeStatus(CreditGradeStatus.UPPER)
            .profileName("프로필1")
            .build();

        Profile profile = Profile.builder()
            .employmentStatus(input.employmentStatus())
            .workplaceName(input.workplaceName())
            .employmentForm(input.employmentForm())
            .income(input.income())
            .employmentDate(input.employmentDate())
            .purposeOfLoan(input.purposeOfLoan())
            .desiredLoanAmount(input.desiredLoanAmount())
            .loanProductUsageStatus(input.loanProductUsageStatus())
            .loanProductUsageCount(input.loanProductUsageCount())
            .totalLoanUsageAmount(input.totalLoanUsageAmount())
            .creditScore(input.creditScore())
            .creditGradeStatus(input.creditGradeStatus())
            .profileName(input.profileName())
            .build();

        when(profileService.createProfile(input.toDto())).thenReturn(profile);

        // when
        ProfileResult result = profileResolver.createProfile(input);

        // then
        assertAll(
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result.purposeOfLoan()).isEqualTo(profile.getPurposeOfLoan()),
            () -> assertThat(result.creditGradeStatus()).isEqualTo(profile.getCreditGradeStatus()),
            () -> assertThat(result.loanProductUsageCount()).isEqualTo(profile.getLoanProductUsageCount()),
            () -> assertThat(result.totalLoanUsageAmount()).isEqualTo(profile.getTotalLoanUsageAmount()),
            () -> assertThat(result.desiredLoanAmount()).isEqualTo(profile.getDesiredLoanAmount()),
            () -> assertThat(result.employmentStatus()).isEqualTo(profile.getEmploymentStatus())
        );
    }
}
