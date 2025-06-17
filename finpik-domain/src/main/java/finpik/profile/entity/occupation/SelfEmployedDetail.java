package finpik.profile.entity.occupation;

import finpik.entity.enums.Occupation;
import finpik.error.enums.ErrorCode;

import java.time.LocalDate;

import static finpik.util.Preconditions.require;

public record SelfEmployedDetail(
    Occupation occupation,
    Integer annualIncome,
    LocalDate businessStartDate
) implements OccupationDetail {
    public SelfEmployedDetail {
        require(businessStartDate, ErrorCode.INVALID_SELF_EMPLOYED_FIELDS.getMessage());
    }

    @Override
    public Occupation getOccupation() {
        return occupation;
    }

    @Override
    public Integer getAnnualIncome() {
        return annualIncome;
    }
}
