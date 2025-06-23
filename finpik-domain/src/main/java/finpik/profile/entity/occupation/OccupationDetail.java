package finpik.profile.entity.occupation;

import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.Occupation;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.profile.entity.policy.ProfileCreationSpec;

import static finpik.util.Preconditions.require;

public sealed interface OccupationDetail
    permits EmployeeDetail, PublicServantDetail, SelfEmployedDetail, FreelancerDetail, OtherDetail
{
    Occupation getOccupation();
    Integer getAnnualIncome();
    EmploymentForm getEmploymentForm();

    static OccupationDetail of(ProfileCreationSpec spec) {
        require(spec.annualIncome(), ErrorCode.INVALID_ANNUAL_INCOME.getMessage());

        return switch (spec.occupation()) {
            case EMPLOYEE -> new EmployeeDetail(
                spec.occupation(),
                spec.annualIncome(),
                spec.employmentForm(),
                spec.employmentDate()
            );

            case PUBLIC_SERVANT -> new PublicServantDetail(
                spec.occupation(),
                spec.annualIncome(),
                spec.employmentDate()
            );

            case SELF_EMPLOYED -> new SelfEmployedDetail(
                spec.occupation(),
                spec.annualIncome(),
                spec.businessStartDate()
            );

            case FREELANCER -> new FreelancerDetail(
                spec.occupation(),
                spec.annualIncome(),
                spec.employmentDate(),
                EmploymentForm.CONTRACT
            );

            case OTHER -> new OtherDetail(
                spec.occupation(),
                spec.annualIncome()
            );

            default -> throw new BusinessException(ErrorCode.UNSUPPORTED_OCCUPATION);
        };
    }
}
