package finpik.profile.entity.occupation;

import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.Occupation;
import finpik.error.enums.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OccupationDetailTest {
    @DisplayName("직업에 따라 만들어지는 구현체가 달라진다.")
    @Test
    void createByOccupationTest() {
        //given
        Integer annualIncome = 1000000;

        OccupationDetail employmentDetail = OccupationDetail.of(
            annualIncome,
            Occupation.EMPLOYEE,
            EmploymentForm.FULL_TIME,
            LocalDate.now(),
            null
        );

        OccupationDetail publicServantDetail = OccupationDetail.of(
            annualIncome,
            Occupation.PUBLIC_SERVANT,
            EmploymentForm.FULL_TIME,
            LocalDate.now(),
            null
        );

        OccupationDetail otherDetail = OccupationDetail.of(
            annualIncome,
            Occupation.OTHER,
            null,
            null,
            null
        );

        OccupationDetail selfEmployedDetail = OccupationDetail.of(
            annualIncome,
            Occupation.SELF_EMPLOYED,
            EmploymentForm.FULL_TIME,
            null,
            LocalDate.now()
        );
        OccupationDetail freelancerDetail = OccupationDetail.of(
            annualIncome,
            Occupation.FREELANCER,
            null,
            LocalDate.now(),
            null
        );

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
        Integer nullAnnualIncome = null;
        Integer negativeAnnualIncome = -1000000;

        //when
        //then
        assertAll(
            () -> assertThatThrownBy(() -> OccupationDetail.of(
                nullAnnualIncome,
                Occupation.EMPLOYEE,
                EmploymentForm.FULL_TIME,
                LocalDate.now(),
                null
            )).hasMessage(ErrorCode.INVALID_ANNUAL_INCOME.getMessage()),

            () -> assertThatThrownBy(() -> OccupationDetail.of(
                negativeAnnualIncome,
                Occupation.EMPLOYEE,
                EmploymentForm.FULL_TIME,
                LocalDate.now(),
                null
            )).hasMessage(ErrorCode.INVALID_ANNUAL_INCOME.getMessage())
        );

    }
}
