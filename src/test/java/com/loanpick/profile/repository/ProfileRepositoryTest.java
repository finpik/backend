package com.loanpick.profile.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;

@DataJpaTest
@ActiveProfiles("test")
class ProfileRepositoryTest {
    @Autowired
    private ProfileRepository profileRepository;

    @AfterEach
    void tearDown() {
        profileRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Profile 저장 후 조회가 가능하다")
    void saveAndFindProfile() {
        // given
        Profile profile = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).income(60000000).workplaceName("Sample Company")
                .profileName("내 프로필").employmentForm(EmploymentForm.FULL_TIME)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).purposeOfLoan(PurposeOfLoan.HOUSING)
                .employmentStatus(EmploymentStatus.EMPLOYEE).creditGradeStatus(CreditGradeStatus.UPPER)
                .employmentDate(LocalDate.of(2020, 1, 1)).build();

        // when
        Profile savedProfile = profileRepository.save(profile);
        Optional<Profile> foundProfile = profileRepository.findById(savedProfile.getId());

        // then
        assertAll(() -> assertThat(foundProfile).isPresent(),
                () -> assertThat(foundProfile.get().getId()).isEqualTo(savedProfile.getId()),
                () -> assertThat(foundProfile.get().getProfileName()).isEqualTo(profile.getProfileName()),
                () -> assertThat(foundProfile.get().getDesiredLoanAmount()).isEqualTo(profile.getDesiredLoanAmount()),
                () -> assertThat(foundProfile.get().getLoanProductUsageCount())
                        .isEqualTo(profile.getLoanProductUsageCount()),
                () -> assertThat(foundProfile.get().getTotalLoanUsageAmount())
                        .isEqualTo(profile.getTotalLoanUsageAmount()),
                () -> assertThat(foundProfile.get().getCreditScore()).isEqualTo(profile.getCreditScore()),
                () -> assertThat(foundProfile.get().getIncome()).isEqualTo(profile.getIncome()),
                () -> assertThat(foundProfile.get().getWorkplaceName()).isEqualTo(profile.getWorkplaceName()),
                () -> assertThat(foundProfile.get().getEmploymentForm()).isEqualTo(profile.getEmploymentForm()),
                () -> assertThat(foundProfile.get().getLoanProductUsageStatus())
                        .isEqualTo(profile.getLoanProductUsageStatus()),
                () -> assertThat(foundProfile.get().getPurposeOfLoan()).isEqualTo(profile.getPurposeOfLoan()),
                () -> assertThat(foundProfile.get().getEmploymentStatus()).isEqualTo(profile.getEmploymentStatus()),
                () -> assertThat(foundProfile.get().getCreditGradeStatus()).isEqualTo(profile.getCreditGradeStatus()),
                () -> assertThat(foundProfile.get().getEmploymentDate()).isEqualTo(profile.getEmploymentDate()));
    }
}
