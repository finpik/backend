package finpik.profile.entity.occupation;

import finpik.entity.enums.Occupation;

public record OtherDetail(
    Occupation occupation,
    Integer annualIncome
) implements OccupationDetail {
    public OtherDetail(Occupation occupation, Integer annualIncome) {
        this.occupation = occupation;
        this.annualIncome = annualIncome == null ? 0 : annualIncome;
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
