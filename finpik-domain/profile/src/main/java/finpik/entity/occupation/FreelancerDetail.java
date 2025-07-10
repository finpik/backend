package finpik.entity.occupation;

import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.Occupation;
import finpik.error.enums.ErrorCode;

import java.time.LocalDate;

import static finpik.util.Preconditions.require;

public record FreelancerDetail(
    Occupation occupation,
    Integer annualIncome,
    LocalDate employmentDate,
    EmploymentForm employmentForm
) implements OccupationDetail {
    public FreelancerDetail {
        require(employmentDate, ErrorCode.INVALID_FREELANCER_FIELDS.getMessage());
        employmentForm = EmploymentForm.CONTRACT;
    }

    @Override
    public Occupation getOccupation() {
        return occupation;
    }

    @Override
    public Integer getAnnualIncome() {
        return annualIncome;
    }

    @Override
    public EmploymentForm getEmploymentForm() {
        return employmentForm;
    }
}
