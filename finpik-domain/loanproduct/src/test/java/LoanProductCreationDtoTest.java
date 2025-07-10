import finpik.dto.LoanProductCreationDto;
import finpik.entity.enums.CertificateRequirement;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.Gender;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.RepaymentPeriodUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoanProductCreationDtoTest {

    @DisplayName("기본 생성 테스트")
    @Test
    void create() {
        //given
        Long id = 1L;
        String productName = "신용대출";
        Integer repaymentPeriod = 12;
        String bankName = "핀픽은행";
        String bankPhoneNumber = "1234-5678";
        String loanAvailableTime = "09:00~18:00";
        Float maxInterestRate = 7.5f;
        Float minInterestRate = 3.2f;
        RepaymentPeriodUnit repaymentPeriodUnit = RepaymentPeriodUnit.MONTH;
        Integer minAge = 20;
        Integer maxAge = 60;
        Gender genderLimit = Gender.ALL;
        Long maxLoanLimitAmount = 50000000L;
        String loanPrerequisite = "연소득 3천 이상";
        String loanTargetGuide = "직장인 대상";
        String certificationRequirementGuide = "재직증명서 필요";
        String loanLimitGuide = "최대 5천만원";
        String repaymentPeriodGuide = "최대 5년";
        String interestRateGuide = "3.2% ~ 7.5%";
        String loanAvailableTimeGuide = "영업시간 내 신청 가능";
        String repaymentFeeGuide = "중도상환수수료 없음";
        String delinquencyInterestRateGuide = "연 10%";
        String notesOnLoan = "대출 전 신중히 검토하세요";
        String preLoanChecklist = "신용등급 확인, 필요 서류 준비";
        CertificateRequirement certificateRequirement = CertificateRequirement.YES;
        Occupation occupation = Occupation.EMPLOYEE;
        String url = "https://finpik.bank/loan/123";
        Integer minCreditScore = 600;
        EmploymentForm employmentForm = EmploymentForm.FULL_TIME;
        String bankImgUrl = "https://finpik.bank/img/bank.png";

        // when
        LoanProductCreationDto dto = new LoanProductCreationDto(
            id, productName, repaymentPeriod, bankName, bankPhoneNumber, loanAvailableTime,
            maxInterestRate, minInterestRate, repaymentPeriodUnit,
            minAge, maxAge, genderLimit, maxLoanLimitAmount,
            loanPrerequisite, loanTargetGuide, certificationRequirementGuide,
            loanLimitGuide, repaymentPeriodGuide, interestRateGuide,
            loanAvailableTimeGuide, repaymentFeeGuide, delinquencyInterestRateGuide,
            notesOnLoan, preLoanChecklist, certificateRequirement, occupation,
            url, minCreditScore, employmentForm, bankImgUrl
        );

        // then
        Assertions.assertAll(
            ()-> assertThat(dto).isNotNull(),
            ()-> assertThat(dto.productName()).isEqualTo("신용대출"),
            ()-> assertThat(dto.repaymentPeriod()).isEqualTo(12),
            ()-> assertThat(dto.genderLimit()).isEqualTo(Gender.ALL),
            ()-> assertThat(dto.repaymentPeriodUnit()).isEqualTo(RepaymentPeriodUnit.MONTH),
            ()-> assertThat(dto.certificateRequirement()).isEqualTo(CertificateRequirement.YES)
        );
    }
}
