package finpik.profile.entity.occupation;

import finpik.entity.enums.Occupation;
import finpik.error.enums.ErrorCode;

import java.time.LocalDate;

import static finpik.util.Preconditions.require;

public record PublicServantDetail(
    Occupation occupation,
    Integer annualIncome,
    LocalDate employmentDate
) implements OccupationDetail{
    public PublicServantDetail {
        require(employmentDate, ErrorCode.INVALID_PUBLIC_SERVANT_FIELDS.getMessage());
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
