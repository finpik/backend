package finpik.profile.entity.score;

import finpik.entity.enums.CreditGradeStatus;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.profile.entity.policy.NiceCreditGradePolicy;

public record CreditScore(
    Integer creditScore,
    CreditGradeStatus creditGradeStatus
) {
    public CreditScore(Integer creditScore, CreditGradeStatus creditGradeStatus) {
        validateCredits(creditScore, creditGradeStatus);

        this.creditScore = creditScore != null
            ? creditScore
            : determineCreditScoreByGradeStatus(creditGradeStatus);

        this.creditGradeStatus = creditGradeStatus != null
            ? creditGradeStatus
            : determineCreditGradeStatusByScore(creditScore);
    }

    private void validateCredits(Integer creditScore, CreditGradeStatus creditGradeStatus) {
        if (creditGradeStatus == null && creditScore == null) {
            throw new BusinessException(ErrorCode.CREDITS_CANNOT_BE_NULL);
        }
    }

    private CreditGradeStatus determineCreditGradeStatusByScore(Integer creditScore) {
        return NiceCreditGradePolicy.fromScore(creditScore);
    }

    private Integer determineCreditScoreByGradeStatus(CreditGradeStatus creditGradeStatus) {
        return NiceCreditGradePolicy.getRange(creditGradeStatus).min();
    }
}
