package finpik.entity.occupation;

import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.Occupation;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;

import java.time.LocalDate;

import static finpik.util.Preconditions.requirePositive;

public sealed interface OccupationDetail
    permits EmployeeDetail, PublicServantDetail, SelfEmployedDetail, FreelancerDetail, OtherDetail
{
    Occupation getOccupation();
    Integer getAnnualIncome();
    EmploymentForm getEmploymentForm();

    static OccupationDetail of(
        Integer annualIncome,
        Occupation occupation,
        EmploymentForm employmentForm,
        LocalDate employmentDate,
        LocalDate businessStartDate
    ) {
        requirePositive(annualIncome, ErrorCode.INVALID_ANNUAL_INCOME.getMessage());

        return switch (occupation) {
            case EMPLOYEE -> new EmployeeDetail(
                occupation,
                annualIncome,
                employmentForm,
                employmentDate
            );

            case PUBLIC_SERVANT -> new PublicServantDetail(
                occupation,
                annualIncome,
                employmentDate
            );

            case SELF_EMPLOYED -> new SelfEmployedDetail(
                occupation,
                annualIncome,
                businessStartDate
            );

            case FREELANCER -> new FreelancerDetail(
                occupation,
                annualIncome,
                employmentDate,
                EmploymentForm.CONTRACT
            );

            case OTHER -> new OtherDetail(
                occupation,
                annualIncome
            );

            default -> throw new BusinessException(ErrorCode.UNSUPPORTED_OCCUPATION);
        };
    }
}
