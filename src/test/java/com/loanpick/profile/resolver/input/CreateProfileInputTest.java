package com.loanpick.profile.resolver.input;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import com.loanpick.profile.entity.enums.Occupation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;
import com.loanpick.profile.service.dto.CreateProfileDto;
import com.loanpick.user.entity.Gender;
import com.loanpick.user.entity.RegistrationType;
import com.loanpick.user.entity.User;

class CreateProfileInputTest {
    @Test
    @DisplayName("CreateProfileInput에서 toDto() 호출 시 값이 정상 매핑된다")
    void toDto_success() {
        // given
        User user = User.builder().id(1L).username("findpick").email("finpick@gmail.com").gender(Gender.MALE)
                .registrationType(RegistrationType.KAKAO).build();

        CreateProfileInput input = CreateProfileInput.builder().employmentStatus(Occupation.EMPLOYEE)
                .workplaceName("Sample Company").employmentForm(EmploymentForm.FULL_TIME).income(60000000)
                .employmentDate(LocalDate.of(2020, 1, 15)).purposeOfLoan(PurposeOfLoan.HOUSING)
                .desiredLoanAmount(30000000).loanProductUsageStatus(LoanProductUsageStatus.USING)
                .loanProductUsageCount(1).totalLoanUsageAmount(50000000).creditScore(750)
                .creditGradeStatus(CreditGradeStatus.UPPER).profileName("프로필A").build();

        // when
        CreateProfileDto dto = input.toDto(user);

        // then
        Assertions.assertAll(() -> assertThat(dto).isNotNull(),
                () -> assertThat(dto.occupation()).isEqualTo(input.occupation()),
                () -> assertThat(dto.workplaceName()).isEqualTo(input.workplaceName()),
                () -> assertThat(dto.employmentForm()).isEqualTo(input.employmentForm()),
                () -> assertThat(dto.income()).isEqualTo(input.income()),
                () -> assertThat(dto.employmentDate()).isEqualTo(input.employmentDate()),
                () -> assertThat(dto.purposeOfLoan()).isEqualTo(input.purposeOfLoan()),
                () -> assertThat(dto.desiredLoanAmount()).isEqualTo(input.desiredLoanAmount()),
                () -> assertThat(dto.loanProductUsageStatus()).isEqualTo(input.loanProductUsageStatus()),
                () -> assertThat(dto.loanProductUsageCount()).isEqualTo(input.loanProductUsageCount()),
                () -> assertThat(dto.totalLoanUsageAmount()).isEqualTo(input.totalLoanUsageAmount()),
                () -> assertThat(dto.creditScore()).isEqualTo(input.creditScore()),
                () -> assertThat(dto.creditGradeStatus()).isEqualTo(input.creditGradeStatus()),
                () -> assertThat(dto.profileName()).isEqualTo(input.profileName()));
    }
}
