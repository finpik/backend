package finpik.entity.profile;

import java.time.LocalDate;
import java.time.LocalDateTime;

import finpik.entity.Profile;
import finpik.entity.enums.BusinessType;
import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.entity.policy.ProfileCreationSpec;
import finpik.entity.user.UserEntity;
import finpik.mapper.profile.OccupationMapper;
import jakarta.persistence.Column;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer desiredLoanAmount;

    private Integer loanProductUsageCount;

    private Integer totalLoanUsageAmount;

    private Integer creditScore;

    private Integer annualIncome;

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

    private LocalDate businessStartDate;

    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private Integer recommendedLoanProductCount;
    private Float minInterestRate;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProfileEntity from(Profile profile, UserEntity userEntity) {
        OccupationMapper mapper = OccupationMapper.from(profile.getOccupationDetail());

        return new ProfileEntity(
            profile.getId(), profile.getDesiredLoanAmount(), profile.getLoanProductUsageCount(),
            profile.getTotalLoanUsageAmount(), profile.getCreditScore().creditScore(),
            mapper.annualIncome(), profile.getProfileName(), profile.getSeq(),
            mapper.employmentForm(), profile.getCreditScore().creditGradeStatus(),
            profile.getLoanProductUsageStatus(), profile.getPurposeOfLoan(),
            mapper.occupation(), profile.getProfileColor(), mapper.employmentDate(),
            mapper.businessStartDate(), mapper.businessType(),
            userEntity, profile.getRecommendedLoanProductCount(), profile.getMinInterestRate(),
            profile.getCreatedAt(), profile.getUpdatedAt()
        );
    }

    public Profile toDomain() {
        ProfileCreationSpec spec = ProfileCreationSpec.rebuild(
            id, desiredLoanAmount, loanProductUsageCount, totalLoanUsageAmount,
            creditScore, profileName, seq, creditGradeStatus, loanProductUsageStatus,
            purposeOfLoan, profileColor, annualIncome, businessStartDate,
            employmentDate, occupation, businessType, employmentForm,
            user.toDomain(), recommendedLoanProductCount,
            minInterestRate, createdAt, updatedAt
        );

        return Profile.withId(spec);
    }

    public void updateAllFields(Profile profile) {
        OccupationMapper mapper = OccupationMapper.from(profile.getOccupationDetail());

        desiredLoanAmount = profile.getDesiredLoanAmount();
        loanProductUsageCount = profile.getLoanProductUsageCount();
        totalLoanUsageAmount = profile.getTotalLoanUsageAmount();
        creditScore = profile.getCreditScore().creditScore();
        annualIncome = mapper.annualIncome();
        profileName = profile.getProfileName();
        seq = profile.getSeq();
        employmentForm = mapper.employmentForm();
        creditGradeStatus = profile.getCreditScore().creditGradeStatus();
        loanProductUsageStatus = profile.getLoanProductUsageStatus();
        purposeOfLoan = profile.getPurposeOfLoan();
        occupation = mapper.occupation();
        profileColor = profile.getProfileColor();
        employmentDate = mapper.employmentDate();
        profileColor = profile.getProfileColor();
        updatedAt = LocalDateTime.now();
    }

    public void updateSeq(Integer seq) {
        this.seq = seq;
    }

    public void updateRecommendedLoanProductCount(Integer recommendedLoanProductCount) {
        this.recommendedLoanProductCount = recommendedLoanProductCount;
    }
}
