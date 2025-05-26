package finpik.kafka.dto;

import finpik.entity.enums.Occupation;
import finpik.entity.enums.PurposeOfLoan;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecommendLoanProductProfileDto {
    Long profileId;
    Integer desiredLimit;
    Integer creditScore;
    PurposeOfLoan purposeOfLoan;
    Occupation occupation;
    Integer age;

    @Builder
    public RecommendLoanProductProfileDto(Long profileId, Integer desiredLimit, Integer creditScore,
            PurposeOfLoan purposeOfLoan, Occupation occupation, Integer age) {
        this.profileId = profileId;
        this.desiredLimit = desiredLimit;
        this.creditScore = creditScore;
        this.purposeOfLoan = purposeOfLoan;
        this.occupation = occupation;
        this.age = age;
    }
}
