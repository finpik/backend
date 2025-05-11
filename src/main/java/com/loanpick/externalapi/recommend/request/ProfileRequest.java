package com.loanpick.externalapi.recommend.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loanpick.common.entity.enums.Occupation;
import com.loanpick.profile.entity.enums.PurposeOfLoan;

import lombok.Builder;

@Builder
public record ProfileRequest(@JsonProperty("desired_limit") Integer desiredLimit,
        @JsonProperty("credit_score") Integer creditScore, @JsonProperty("purpose_of_loan") PurposeOfLoan purposeOfLoan,
        Occupation occupation, Integer age, Float similarity) {

}
