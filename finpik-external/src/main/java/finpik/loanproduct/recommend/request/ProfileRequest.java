package finpik.loanproduct.recommend.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import finpik.entity.enums.Occupation;
import finpik.entity.enums.PurposeOfLoan;
import lombok.Builder;

@Builder
public record ProfileRequest(@JsonProperty("desired_limit") Integer desiredLimit,
        @JsonProperty("credit_score") Integer creditScore, @JsonProperty("purpose_of_loan") PurposeOfLoan purposeOfLoan,
        Occupation occupation, Integer age, Float similarity) {

}
