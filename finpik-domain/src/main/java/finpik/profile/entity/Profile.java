package finpik.profile.entity;

import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.profile.entity.occupation.OccupationDetail;
import finpik.profile.entity.policy.ProfileCreationSpec;
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
    private LoanProductUsageStatus loanProductUsageStatus;
    private PurposeOfLoan purposeOfLoan;
    private ProfileColor profileColor;
    private OccupationDetail occupationDetail;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Profile of(ProfileCreationSpec spec) {
        LocalDateTime createdAt = LocalDateTime.now();
        return create(spec, null, createdAt, null);
    }

    public static Profile withId(ProfileCreationSpec spec) {
        require(spec.id(), "profile id must not be null.");

        return create(spec, spec.id(), spec.createdAt(), spec.updatedAt());
    }

    private static Profile create(ProfileCreationSpec spec, Long id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        CreditScore creditScore = new CreditScore(spec.creditScore(), spec.creditGradeStatus());
        OccupationDetail occupationDetail = OccupationDetail.of(spec);

        return new Profile(
            id,
            require(spec.desiredLoanAmount(), "desiredLoanAmount must not be null."),
            require(spec.loanProductUsageCount(), "loanProductUsageCount must not be null."),
            require(spec.totalLoanUsageAmount(), "totalLoanUsageAmount must not be null."),
            creditScore,
            require(spec.profileName(), "profileName must not be null."),
            require(spec.seq(), "seq must not be null."),
            require(spec.loanProductUsageStatus(), "loanProductUsageStatus must not be null."),
            require(spec.purposeOfLoan(), "purposeOfLoan must not be null."),
            require(spec.profileColor(), "profileColor must not be null."),
            occupationDetail,
            require(spec.user(), "user must not be null."),
            require(createdAt, "createdAt must not be null."),
            updatedAt
        );
    }

    public void updateProfile(Profile updatedProfile) {
        this.occupationDetail = updatedProfile.getOccupationDetail();
        this.desiredLoanAmount = updatedProfile.getDesiredLoanAmount();
        this.loanProductUsageCount = updatedProfile.getLoanProductUsageCount();
        this.totalLoanUsageAmount = updatedProfile.getTotalLoanUsageAmount();
        this.creditScore = updatedProfile.getCreditScore();
        this.loanProductUsageStatus = updatedProfile.getLoanProductUsageStatus();
        this.purposeOfLoan = updatedProfile.getPurposeOfLoan();
        this.profileName = updatedProfile.getProfileName();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSequence(Integer seq) {
        this.seq = seq;
    }

    public void changeProfileColor(ProfileColor profileColor) {
        this.profileColor = profileColor;
    }
}
