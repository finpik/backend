package finpik.mapper.profile;

import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.Occupation;
import finpik.profile.entity.occupation.EmployeeDetail;
import finpik.profile.entity.occupation.FreelancerDetail;
import finpik.profile.entity.occupation.OccupationDetail;
import finpik.profile.entity.occupation.OtherDetail;
import finpik.profile.entity.occupation.PublicServantDetail;
import finpik.profile.entity.occupation.SelfEmployedDetail;

import java.time.LocalDate;

public record OccupationMapper(
    Occupation occupation,
    Integer annualIncome,
    EmploymentForm employmentForm,
    LocalDate employmentDate,
    LocalDate businessStartDate
) {
    public static OccupationMapper from(OccupationDetail detail) {
        return switch (detail) {
            case EmployeeDetail e ->
                new OccupationMapper(
                    e.occupation(), e.annualIncome(), e.employmentForm(),
                    e.employmentDate(), null
                );
            case PublicServantDetail p ->
                new OccupationMapper(
                    p.occupation(), p.annualIncome(), null,
                    p.employmentDate(), null
                );
            case SelfEmployedDetail s ->
                new OccupationMapper(s.occupation(), s.annualIncome(),
                    null, null, s.businessStartDate()
                );
            case FreelancerDetail f ->
                new OccupationMapper(f.occupation(), f.annualIncome(),
                    null, f.employmentDate(), null
                );
            case OtherDetail o ->
                new OccupationMapper(
                    o.occupation(), o.annualIncome(),
                    null, null, null
                );
        };
    }
}
