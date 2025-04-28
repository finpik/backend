package com.loanpick.profile.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;
import com.loanpick.profile.repository.ProfileRepository;
import com.loanpick.profile.service.dto.CreateProfileDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class ProfileServiceImplTest {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;

    @AfterEach
    void tearDown() {
        profileRepository.deleteAll();
    }

    @Test
    @DisplayName("createProfile 호출 시 Profile이 저장된다")
    void createProfile_success() {
        // given
        CreateProfileDto dto = CreateProfileDto.builder()
            .desiredLoanAmount(10000000)
            .loanProductUsageCount(2)
            .totalLoanUsageAmount(5000000)
            .creditScore(750)
            .income(60000000)
            .workplaceName("Sample Company")
            .profileName("내 프로필")
            .employmentForm(EmploymentForm.FULL_TIME)
            .loanProductUsageStatus(LoanProductUsageStatus.USING)
            .purposeOfLoan(PurposeOfLoan.HOUSING)
            .employmentStatus(EmploymentStatus.EMPLOYEE)
            .creditGradeStatus(CreditGradeStatus.UPPER)
            .employmentDate(LocalDate.of(2020, 1, 1))
            .build();

        // when
        Profile savedProfile = profileService.createProfile(dto);

        // then
        assertAll(
            () -> assertThat(savedProfile.getId()).isNotNull(),
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
            () -> assertThat(savedProfile.getEmploymentDate()).isEqualTo(dto.employmentDate())
        );
    }
}
