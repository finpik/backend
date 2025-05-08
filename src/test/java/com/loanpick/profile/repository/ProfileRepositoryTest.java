package com.loanpick.profile.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

import com.loanpick.profile.entity.enums.Occupation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;
import com.loanpick.user.entity.Gender;
import com.loanpick.user.entity.RegistrationType;
import com.loanpick.user.entity.User;
import com.loanpick.user.repository.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
class ProfileRepositoryTest {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        profileRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("Profile 저장 후 조회가 가능하다")
    @Test
    void saveAndFindProfile() {
        // given
        Profile profile = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).profileName("내 프로필")
                .employmentForm(EmploymentForm.FULL_TIME).loanProductUsageStatus(LoanProductUsageStatus.USING)
                .purposeOfLoan(PurposeOfLoan.HOUSING).employmentStatus(Occupation.SELF_EMPLOYED)
                .creditGradeStatus(CreditGradeStatus.UPPER).build();

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
                () -> assertThat(foundProfile.get().getOccupation()).isEqualTo(profile.getOccupation()),
                () -> assertThat(foundProfile.get().getCreditGradeStatus()).isEqualTo(profile.getCreditGradeStatus()),
                () -> assertThat(foundProfile.get().getEmploymentDate()).isEqualTo(profile.getEmploymentDate()));
    }

    @DisplayName("유저 기준 프로필을 조회할 수 있다.")
    @Test
    void findByUser() {
        // given
        User user = User.builder().username("findpick").email("finpick@gmail.com").gender(Gender.MALE)
                .registrationType(RegistrationType.KAKAO).build();

        User savedUser = userRepository.save(user);

        Profile profileFirst = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).profileName("프로필1")
                .employmentForm(EmploymentForm.FULL_TIME).loanProductUsageStatus(LoanProductUsageStatus.USING)
                .purposeOfLoan(PurposeOfLoan.HOUSING).employmentStatus(Occupation.SELF_EMPLOYED)
                .creditGradeStatus(CreditGradeStatus.UPPER).user(savedUser).seq(0).build();

        Profile profileSecond = Profile.builder().desiredLoanAmount(10000000).loanProductUsageCount(2)
                .totalLoanUsageAmount(5000000).creditScore(750).profileName("프로필2")
                .employmentForm(EmploymentForm.FULL_TIME).loanProductUsageStatus(LoanProductUsageStatus.USING)
                .purposeOfLoan(PurposeOfLoan.HOUSING).employmentStatus(Occupation.SELF_EMPLOYED)
                .creditGradeStatus(CreditGradeStatus.UPPER).user(savedUser).seq(1).build();

        List<Profile> profileList = List.of(profileFirst, profileSecond);
        profileRepository.saveAll(profileList);

        // when
        List<Profile> foundProfileList = profileRepository.findByUser(savedUser);

        // then
        assertAll(() -> assertThat(2).isEqualTo(profileList.size()),
                () -> assertThat("프로필1").isEqualTo(foundProfileList.get(0).getProfileName()),
                () -> assertThat("프로필2").isEqualTo(foundProfileList.get(1).getProfileName()));
    }
}
