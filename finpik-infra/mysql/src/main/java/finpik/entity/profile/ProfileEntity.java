package finpik.entity.profile;

import java.time.LocalDate;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.entity.user.UserEntity;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.profile.entity.Profile;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer desiredLoanAmount;

    private Integer loanProductUsageCount;

    private Integer totalLoanUsageAmount;

    private Integer creditScore;

    private Integer income;

    private String workplaceName;

    private String profileName;

    @Description("프로필의 보여지는 순번")
    private Integer seq;

    @Enumerated(EnumType.STRING)
    private EmploymentForm employmentForm;

    @Enumerated(EnumType.STRING)
    private CreditGradeStatus creditGradeStatus;

    @Enumerated(EnumType.STRING)
    private LoanProductUsageStatus loanProductUsageStatus;

    @Enumerated(EnumType.STRING)
    private PurposeOfLoan purposeOfLoan;

    @Enumerated(EnumType.STRING)
    private Occupation occupation;

    @Enumerated(EnumType.STRING)
    private ProfileColor profileColor;

    private LocalDate employmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public ProfileEntity(Integer desiredLoanAmount, Integer loanProductUsageCount, Integer totalLoanUsageAmount,
            Integer creditScore, CreditGradeStatus creditGradeStatus, Integer income, Integer seq, String workplaceName,
            EmploymentForm employmentForm, LoanProductUsageStatus loanProductUsageStatus, PurposeOfLoan purposeOfLoan,
            LocalDate employmentDate, String profileName, Occupation occupation, UserEntity user,
            ProfileColor profileColor) {
        this.desiredLoanAmount = desiredLoanAmount;
        this.loanProductUsageCount = loanProductUsageCount;
        this.totalLoanUsageAmount = totalLoanUsageAmount;
        this.creditScore = creditScore;
        this.creditGradeStatus = creditGradeStatus;
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

    public static ProfileEntity from(Profile profile, UserEntity userEntity) {
        return ProfileEntity.builder().purposeOfLoan(profile.getPurposeOfLoan())
                .desiredLoanAmount(profile.getDesiredLoanAmount())
                .loanProductUsageCount(profile.getLoanProductUsageCount())
                .totalLoanUsageAmount(profile.getTotalLoanUsageAmount()).creditScore(profile.getCreditScore())
                .creditGradeStatus(profile.getCreditGradeStatus()).income(profile.getIncome())
                .workplaceName(profile.getWorkplaceName()).profileName(profile.getProfileName())
                .occupation(profile.getOccupation()).profileColor(profile.getProfileColor())
                .employmentDate(profile.getEmploymentDate()).user(userEntity).profileColor(profile.getProfileColor())
                .seq(profile.getSeq()).build();
    }

    public Profile toDomain() {
        return Profile.builder().id(id).desiredLoanAmount(desiredLoanAmount)
                .loanProductUsageCount(loanProductUsageCount).totalLoanUsageAmount(totalLoanUsageAmount)
                .creditScore(creditScore).income(income).workplaceName(workplaceName).profileName(profileName).seq(seq)
                .employmentForm(employmentForm).creditGradeStatus(creditGradeStatus)
                .loanProductUsageStatus(loanProductUsageStatus).purposeOfLoan(purposeOfLoan).occupation(occupation)
                .profileColor(profileColor).employmentDate(employmentDate).user(user.toDomain()).build();
    }

    public void updateAllFields(Profile profile) {
        desiredLoanAmount = profile.getDesiredLoanAmount();
        loanProductUsageCount = profile.getLoanProductUsageCount();
        totalLoanUsageAmount = profile.getTotalLoanUsageAmount();
        creditScore = profile.getCreditScore();
        income = profile.getIncome();
        workplaceName = profile.getWorkplaceName();
        profileName = profile.getProfileName();
        seq = profile.getSeq();
        employmentForm = profile.getEmploymentForm();
        creditGradeStatus = profile.getCreditGradeStatus();
        loanProductUsageStatus = profile.getLoanProductUsageStatus();
        purposeOfLoan = profile.getPurposeOfLoan();
        occupation = profile.getOccupation();
        profileColor = profile.getProfileColor();
        employmentDate = profile.getEmploymentDate();
        profileColor = profile.getProfileColor();
    }

    public void updateSequence(Integer seq) {
        if (seq == null) {
            throw new BusinessException(ErrorCode.PROFILE_SEQUENCE_CANNOT_BE_NULL);
        }

        this.seq = seq;
    }
}
