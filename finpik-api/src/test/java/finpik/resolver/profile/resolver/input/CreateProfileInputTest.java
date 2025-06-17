package finpik.resolver.profile.resolver.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import finpik.entity.enums.Occupation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.resolver.profile.application.dto.CreateProfileUseCaseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateProfileInputTest {
    private Validator validator;

    @BeforeAll
    void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @DisplayName("유효하지 않은 CreateProfileInput - 제약 조건 테스트")
    @MethodSource("provideInvalidInputs")
    @ParameterizedTest(name = "[{index}] invalid input: {0}")
    void invalidInputs(CreateProfileInput input, String expectedMessage) {
        // given
        Set<ConstraintViolation<CreateProfileInput>> violations = validator.validate(input);

        // when
        // then
        assertAll(() -> assertThat(violations.isEmpty()).isEqualTo(false),
                () -> assertThat(violations).anyMatch(it -> it.getMessage().equals(expectedMessage)));
    }

    @DisplayName("유효한 CreateProfileInput DTO 변환 테스트")
    @Test
    void toDto() {
        // given
        Long userId = 1L;

        CreateProfileInput input = CreateProfileInput.builder().occupation(Occupation.EMPLOYEE)
                .employmentForm(EmploymentForm.CONTRACT).annualIncome(50000000)
                .employmentDate(LocalDate.of(2020, 1, 1)).purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT)
                .desiredLoanAmount(10000000).loanProductUsageStatus(LoanProductUsageStatus.NOT_USING)
                .loanProductUsageCount(2).totalLoanUsageAmount(5000000).creditScore(750)
                .creditGradeStatus(CreditGradeStatus.EXCELLENT).profileName("테스트 프로필")
                .profileColor(ProfileColor.GRAY_TWO).build();

        // when
        CreateProfileUseCaseDto dto = input.toDto(userId);

        // then
        assertAll(() -> assertThat(dto).isNotNull(), () -> assertThat(dto.userId()).isEqualTo(userId),
                () -> assertThat(dto.occupation()).isEqualTo(input.occupation()),
                () -> assertThat(dto.profileName()).isEqualTo(input.profileName()),
                () -> assertThat(dto.profileColor()).isEqualTo(input.profileColor()));
    }

    Stream<Arguments> provideInvalidInputs() {
        return Stream.of(
                Arguments.of(
                        CreateProfileInput.builder().occupation(null).purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT)
                                .desiredLoanAmount(100000).loanProductUsageStatus(LoanProductUsageStatus.USING)
                                .loanProductUsageCount(1).totalLoanUsageAmount(10000).profileName("유효한이름").build(),
                        "직업을 선택해 주세요."),
                Arguments.of(
                        CreateProfileInput.builder().occupation(Occupation.EMPLOYEE).purposeOfLoan(null)
                                .desiredLoanAmount(100000).loanProductUsageStatus(LoanProductUsageStatus.USING)
                                .loanProductUsageCount(1).totalLoanUsageAmount(10000).profileName("유효한이름").build(),
                        "대출 목적을 선택해 주세요."),
                Arguments.of(CreateProfileInput.builder().occupation(Occupation.EMPLOYEE)
                        .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(null)
                        .loanProductUsageStatus(LoanProductUsageStatus.USING).loanProductUsageCount(1)
                        .totalLoanUsageAmount(10000).profileName("유효한이름").build(), "대출 희망 금액을 적어주세요."),
                Arguments.of(CreateProfileInput.builder().occupation(Occupation.EMPLOYEE)
                        .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(-1000)
                        .loanProductUsageStatus(LoanProductUsageStatus.USING).loanProductUsageCount(1)
                        .totalLoanUsageAmount(10000).profileName("유효한이름").build(), "대출 희망 금액은 0원 이상 작성해주세요."),
                Arguments.of(CreateProfileInput.builder().occupation(Occupation.EMPLOYEE)
                        .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(10000)
                        .loanProductUsageStatus(null).loanProductUsageCount(1).totalLoanUsageAmount(10000)
                        .profileName("유효한이름").build(), "이용하는 서비스가 있는지 선택해주세요."),
                Arguments.of(CreateProfileInput.builder().occupation(Occupation.EMPLOYEE)
                        .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(10000)
                        .loanProductUsageStatus(LoanProductUsageStatus.USING).loanProductUsageCount(null)
                        .totalLoanUsageAmount(10000).profileName("유효한이름").build(), "현재 가입하신 대출 개수를 적어주세요."),
                Arguments.of(CreateProfileInput.builder().occupation(Occupation.EMPLOYEE)
                        .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(10000)
                        .loanProductUsageStatus(LoanProductUsageStatus.USING).loanProductUsageCount(-1)
                        .totalLoanUsageAmount(10000).profileName("유효한이름").build(), "대출 개수는 0개 이상 작성해주세요."),
                Arguments.of(CreateProfileInput.builder().occupation(Occupation.EMPLOYEE)
                        .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(10000)
                        .loanProductUsageStatus(LoanProductUsageStatus.USING).loanProductUsageCount(1)
                        .totalLoanUsageAmount(null).profileName("유효한이름").build(), "현재 가입하신 모든 대출 금액을 적어주세요."),
                Arguments.of(CreateProfileInput.builder().occupation(Occupation.EMPLOYEE)
                        .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(10000)
                        .loanProductUsageStatus(LoanProductUsageStatus.USING).loanProductUsageCount(1)
                        .totalLoanUsageAmount(-1).profileName("유효한이름").build(), "대출 금액은 0원 이상 작성해주세요."),
                Arguments.of(CreateProfileInput.builder().occupation(Occupation.EMPLOYEE)
                        .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(10000)
                        .loanProductUsageStatus(LoanProductUsageStatus.USING).loanProductUsageCount(1)
                        .totalLoanUsageAmount(10000).profileName("") // blank
                        .build(), "작성하신 프로필의 이름을 만들어주세요."),
                Arguments.of(CreateProfileInput.builder().occupation(Occupation.EMPLOYEE)
                        .purposeOfLoan(PurposeOfLoan.LOAN_REPAYMENT).desiredLoanAmount(10000)
                        .loanProductUsageStatus(LoanProductUsageStatus.USING).loanProductUsageCount(1)
                        .totalLoanUsageAmount(10000).profileName("15자가넘어가는이름입니다이건") // 15자 이상
                        .build(), "프로필 이름은 1자 이상 14자 이하로 작성해주세요."));
    }
}
