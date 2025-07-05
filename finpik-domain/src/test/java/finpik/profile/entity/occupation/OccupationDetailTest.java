package finpik.profile.entity.occupation;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.Gender;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.entity.enums.RegistrationType;
import finpik.error.enums.ErrorCode;
import finpik.profile.entity.policy.ProfileCreationSpec;
import finpik.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OccupationDetailTest {
    @DisplayName("직업에 따라 만들어지는 구현체가 달라진다.")
    @Test
    void createByOccupationTest() {
        //given
        OccupationDetail employmentDetail = OccupationDetail.of(createDefault(Occupation.EMPLOYEE, 0));
        OccupationDetail publicServantDetail = OccupationDetail.of(createDefault(Occupation.PUBLIC_SERVANT, 0));
        OccupationDetail otherDetail = OccupationDetail.of(createDefault(Occupation.OTHER, 0));
        OccupationDetail selfEmployedDetail = OccupationDetail.of(createDefault(Occupation.SELF_EMPLOYED, 0));
        OccupationDetail freelancerDetail = OccupationDetail.of(createDefault(Occupation.FREELANCER, 0));

        //when
        //then
        assertAll(
            () -> assertThat(employmentDetail).isInstanceOf(EmployeeDetail.class),
            () -> assertThat(publicServantDetail).isInstanceOf(PublicServantDetail.class),
            () -> assertThat(otherDetail).isInstanceOf(OtherDetail.class),
            () -> assertThat(selfEmployedDetail).isInstanceOf(SelfEmployedDetail.class),
            () -> assertThat(freelancerDetail).isInstanceOf(FreelancerDetail.class)
        );
    }

    @DisplayName("연 소득이 Null이거나 음수일 경우 에러가 발생한다.")
    @Test
    void exceptionNotSupportedOccupationTest() {
        //given
        //when
        //then
        assertAll(
            () -> assertThatThrownBy(() -> OccupationDetail.of(createDefault(Occupation.EMPLOYEE, null)))
                .hasMessage(ErrorCode.INVALID_ANNUAL_INCOME.getMessage()),
            () -> assertThatThrownBy(() -> OccupationDetail.of(createDefault(Occupation.EMPLOYEE, -1)))
                .hasMessage(ErrorCode.INVALID_ANNUAL_INCOME.getMessage())
        );

    }

    private ProfileCreationSpec createDefault(Occupation occupation, Integer annualIncome) {
        User user = User.withId(
            1L,
            "테스트유저",
            "test@example.com",
            Gender.MALE,
            RegistrationType.KAKAO,
            LocalDateTime.of(2023, 1, 1, 12, 0),
            LocalDate.of(1990, 1, 1)
        );

        return new ProfileCreationSpec(
            1L,
            1000000,
            2,
            500000,
            750,
            "테스트 프로필",
            1,
            CreditGradeStatus.GOOD,
            LoanProductUsageStatus.USING,
            PurposeOfLoan.LOAN_REPAYMENT,
            ProfileColor.GRAY_TWO,
            annualIncome, // annualIncome
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2021, 1, 1),
            occupation,
            EmploymentForm.FULL_TIME,
            user,
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now()
        );
    }
}
