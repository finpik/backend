package finpik.profile.entity;

import java.time.LocalDate;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.profile.entity.policy.NiceCreditGradePolicy;
import finpik.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Profile {
    private Long id;
    private Integer desiredLoanAmount;
    private Integer loanProductUsageCount;
    private Integer totalLoanUsageAmount;
    private Integer creditScore;
    private Integer income;
    private String workplaceName;
    private String profileName;
    private Integer seq;
    private EmploymentForm employmentForm;
    private CreditGradeStatus creditGradeStatus;
    private LoanProductUsageStatus loanProductUsageStatus;
    private PurposeOfLoan purposeOfLoan;
    private Occupation occupation;
    private ProfileColor profileColor;
    private LocalDate employmentDate;
    private User user;

    public Profile(
        Integer desiredLoanAmount, Integer loanProductUsageCount, Integer totalLoanUsageAmount,
        Integer creditScore, CreditGradeStatus creditGradeStatus, Integer income, Integer seq,
        String workplaceName, EmploymentForm employmentForm, LoanProductUsageStatus loanProductUsageStatus,
        PurposeOfLoan purposeOfLoan, LocalDate employmentDate, String profileName,
        Occupation occupation, User user, Long id, ProfileColor profileColor
    ) {
        validateInfoRelatedEmploymentStatus(occupation, income, workplaceName, employmentDate);

        this.id = id;
        this.desiredLoanAmount = desiredLoanAmount;
        this.loanProductUsageCount = loanProductUsageCount;
        this.totalLoanUsageAmount = totalLoanUsageAmount;
        this.creditScore = creditScore;
        this.creditGradeStatus = creditGradeStatus == null ? determineCreditGradeStatusByScore() : creditGradeStatus;
        this.income = income;
        this.workplaceName = workplaceName;
        this.employmentForm = employmentForm;
        this.loanProductUsageStatus = loanProductUsageStatus;
        this.purposeOfLoan = purposeOfLoan;
        this.employmentDate = employmentDate;
        this.profileName = profileName;
        this.occupation = occupation;
        this.user = user;
        this.profileColor = profileColor;
        this.seq = seq == null ? 0 : seq;
    }

    public static Profile of(
        Integer desiredLoanAmount, Integer loanProductUsageCount, Integer totalLoanUsageAmount,
        Integer creditScore, CreditGradeStatus creditGradeStatus, Integer income, Integer seq,
        String workplaceName, EmploymentForm employmentForm, LoanProductUsageStatus loanProductUsageStatus,
        PurposeOfLoan purposeOfLoan, LocalDate employmentDate, String profileName,
        Occupation occupation, User user, ProfileColor profileColor
    ) {
        return new Profile(
            desiredLoanAmount, loanProductUsageCount, totalLoanUsageAmount,
            creditScore, creditGradeStatus, income, seq == null ? 0 : seq,
            workplaceName, employmentForm, loanProductUsageStatus,
            purposeOfLoan, employmentDate, profileName,
            occupation, user, null, profileColor
        );
    }

    public static Profile withId(
        Long id, Integer desiredLoanAmount, Integer loanProductUsageCount, Integer totalLoanUsageAmount,
        Integer creditScore, CreditGradeStatus creditGradeStatus, Integer income, Integer seq,
        String workplaceName, EmploymentForm employmentForm, LoanProductUsageStatus loanProductUsageStatus,
        PurposeOfLoan purposeOfLoan, LocalDate employmentDate, String profileName,
        Occupation occupation, User user, ProfileColor profileColor
    ) {
        return new Profile(
            desiredLoanAmount, loanProductUsageCount, totalLoanUsageAmount,
            creditScore, creditGradeStatus, income, seq == null ? 0 : seq,
            workplaceName, employmentForm, loanProductUsageStatus,
            purposeOfLoan, employmentDate, profileName,
            occupation, user, id, profileColor
        );
    }

    public void updateProfile(Profile profile) {
        updateInfoRelatedEmploymentStatus(
            profile.getOccupation(), profile.getIncome(),
            profile.getWorkplaceName(), profile.getEmploymentDate()
        );

        this.desiredLoanAmount = profile.getDesiredLoanAmount();
        this.loanProductUsageCount = profile.getLoanProductUsageCount();
        this.totalLoanUsageAmount = profile.getTotalLoanUsageAmount();
        this.creditScore = profile.getCreditScore();
        this.creditGradeStatus = profile.getCreditGradeStatus();
        this.employmentForm = profile.getEmploymentForm();
        this.loanProductUsageStatus = profile.getLoanProductUsageStatus();
        this.purposeOfLoan = profile.getPurposeOfLoan();
        this.profileName = profile.getProfileName();
    }

    public void updateSequence(Integer seq) {
        this.seq = seq;
    }

    private void validateInfoRelatedEmploymentStatus(
        Occupation occupation,
        Integer income,
        String workplaceName,
        LocalDate employmentDate
    ) {
        if (occupation == Occupation.EMPLOYEE) {
            if (income == null || workplaceName == null || employmentDate == null) {
                throw new BusinessException(ErrorCode.INVALID_EMPLOYMENT_INFO);
            }
        } else {
            if (income != null || workplaceName != null || employmentDate != null) {
                throw new BusinessException(ErrorCode.INVALID_NOT_EMPLOYMENT_INFO);
            }
        }
    }

    private CreditGradeStatus determineCreditGradeStatusByScore() {
        validateCredits();

        return NiceCreditGradePolicy.fromScore(creditScore);
    }

    private void validateCredits() {
        if (creditGradeStatus == null && creditScore == null) {
            throw new BusinessException(ErrorCode.CREDITS_CANNOT_BE_NULL);
        }
    }

    public void updateInfoRelatedEmploymentStatus(
        Occupation occupation,
        Integer income,
        String workplaceName,
        LocalDate employmentDate
    ) {
        if (occupation == Occupation.EMPLOYEE) {
            if (income == null || workplaceName == null || employmentForm == null || employmentDate == null) {
                throw new BusinessException(ErrorCode.INVALID_EMPLOYMENT_INFO);
            } else {
                this.occupation = occupation;
                this.income = income;
                this.workplaceName = workplaceName;
                this.employmentDate = employmentDate;
            }
        } else {
            this.occupation = occupation;
            this.income = null;
            this.workplaceName = null;
            this.employmentDate = null;
        }
    }

    public void changeProfileColor(ProfileColor profileColor) {
        this.profileColor = profileColor;
    }
}
