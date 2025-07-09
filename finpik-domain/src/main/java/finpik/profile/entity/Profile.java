package finpik.profile.entity;

import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.profile.entity.occupation.OccupationDetail;
import finpik.profile.entity.policy.ProfileCreationSpec;
import finpik.profile.entity.policy.ProfileUpdateSpec;
import finpik.profile.entity.score.CreditScore;
import finpik.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static finpik.util.Preconditions.require;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Profile {
    private Long id;
    private Integer desiredLoanAmount;
    private Integer loanProductUsageCount;
    private Integer totalLoanUsageAmount;
    private CreditScore creditScore;
    private String profileName;
    private Integer seq;
    //TODO loanusage 사용필요
    private LoanProductUsageStatus loanProductUsageStatus;
    private PurposeOfLoan purposeOfLoan;
    private ProfileColor profileColor;
    private OccupationDetail occupationDetail;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Profile of(ProfileCreationSpec spec) {
        return create(spec);
    }

    public static Profile withId(ProfileCreationSpec spec) {
        require(spec.id(), "profile id must not be null.");

        return create(spec);
    }

    private static Profile create(
        ProfileCreationSpec spec
    ) {
        CreditScore creditScore = new CreditScore(spec.creditScore(), spec.creditGradeStatus());
        OccupationDetail occupationDetail = OccupationDetail.of(
            spec.annualIncome(), spec.occupation(), spec.employmentForm(),
            spec.employmentDate(), spec.businessStartDate()
        );

        LocalDateTime createdAt = LocalDateTime.now();

        return new Profile(
            spec.id(),
            require(spec.desiredLoanAmount(), "desiredLoanAmount must not be null."),
            require(spec.loanProductUsageCount(), "loanProductUsageCount must not be null."),
            require(spec.totalLoanUsageAmount(), "totalLoanUsageAmount must not be null."),
            creditScore,
            require(spec.profileName(), "profileName must not be null."),
            spec.seq() == null ? 0 : spec.seq() ,
            require(spec.loanProductUsageStatus(), "loanProductUsageStatus must not be null."),
            require(spec.purposeOfLoan(), "purposeOfLoan must not be null."),
            require(spec.profileColor(), "profileColor must not be null."),
            occupationDetail,
            require(spec.user(), "user must not be null."),
            spec.createdAt() == null ? createdAt : spec.createdAt(),
            spec.updatedAt()
        );
    }

    public void updateProfile(ProfileUpdateSpec spec) {
        OccupationDetail occupationDetail = OccupationDetail.of(
            spec.annualIncome(),
            spec.occupation(),
            spec.employmentForm(),
            spec.employmentDate(),
            spec.businessStartDate()
        );

        CreditScore creditScore = new CreditScore(spec.creditScore(), spec.creditGradeStatus());

        this.occupationDetail = occupationDetail;
        this.purposeOfLoan = spec.purposeOfLoan() == null ? this.purposeOfLoan : spec.purposeOfLoan();
        this.desiredLoanAmount = spec.desiredLoanAmount() == null ? this.desiredLoanAmount : spec.desiredLoanAmount();
        this.loanProductUsageStatus = spec.loanProductUsageStatus() == null ? this.loanProductUsageStatus : spec.loanProductUsageStatus();
        this.loanProductUsageCount = spec.loanProductUsageCount() == null ? this.loanProductUsageCount : spec.loanProductUsageCount();
        this.totalLoanUsageAmount = spec.totalLoanUsageAmount() == null ? this.totalLoanUsageAmount : spec.totalLoanUsageAmount();
        this.creditScore = creditScore;
        this.profileName = spec.profileName() == null ? this.profileName : spec.profileName();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSequence(Integer seq) {
        this.seq = seq;
    }

    public void changeProfileColor(ProfileColor profileColor) {
        this.profileColor = profileColor;
    }
}
