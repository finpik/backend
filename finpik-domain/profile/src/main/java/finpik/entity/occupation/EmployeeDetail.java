package finpik.entity.occupation;

import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.Occupation;
import finpik.error.enums.ErrorCode;

import java.time.LocalDate;

import static finpik.util.Preconditions.require;

public record EmployeeDetail(
    Occupation occupation,
    Integer annualIncome,
    EmploymentForm employmentForm,
    LocalDate employmentDate
) implements OccupationDetail {
    public EmployeeDetail(Occupation occupation, Integer annualIncome, EmploymentForm employmentForm, LocalDate employmentDate) {
        this.occupation = occupation;
        this.annualIncome = annualIncome;
        this.employmentForm = require(employmentForm, ErrorCode.INVALID_EMPLOYMENT_FIELDS.getMessage());
        this.employmentDate = require(employmentDate, ErrorCode.INVALID_EMPLOYMENT_FIELDS.getMessage());
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
