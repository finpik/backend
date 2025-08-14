package finpik.mapper.profile;

import finpik.entity.enums.BusinessType;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.Occupation;
import finpik.entity.occupation.EmployeeDetail;
import finpik.entity.occupation.FreelancerDetail;
import finpik.entity.occupation.OccupationDetail;
import finpik.entity.occupation.OtherDetail;
import finpik.entity.occupation.PublicServantDetail;
import finpik.entity.occupation.SelfEmployedDetail;

import java.time.LocalDate;

public record OccupationMapper(
    Occupation occupation,
    Integer annualIncome,
    EmploymentForm employmentForm,
    LocalDate employmentDate,
    LocalDate businessStartDate,
    BusinessType businessType
) {
    public static OccupationMapper from(OccupationDetail detail) {
        return switch (detail) {
            case EmployeeDetail e ->
                new OccupationMapper(
                    e.occupation(), e.annualIncome(), e.employmentForm(),
                    e.employmentDate(), null, null
                );
            case PublicServantDetail p ->
                new OccupationMapper(
                    p.occupation(), p.annualIncome(), null,
                    p.employmentDate(), null, null
                );
            case SelfEmployedDetail s ->
                new OccupationMapper(s.occupation(), s.annualIncome(),
                    null, null,
                    s.businessStartDate(), s.businessType()
                );
            case FreelancerDetail f ->
                new OccupationMapper(f.occupation(), f.annualIncome(),
                    null, f.employmentDate(),
                    null, null
                );
            case OtherDetail o ->
                new OccupationMapper(
                    o.occupation(), o.annualIncome(),
                    null, null,
                    null, null
                );
        };
    }
}
