package com.loanpick.loanproduct.service.dto;

import com.loanpick.externalapi.recommend.request.ProfileRequest;
import com.loanpick.profile.entity.Profile;
import com.loanpick.common.entity.enums.Occupation;
import com.loanpick.profile.entity.enums.PurposeOfLoan;
import com.loanpick.user.entity.User;
import lombok.Getter;

@Getter
public class RecommendLoanProductProfileDto {
    Long profileId;
    Integer desiredLimit;
    Integer creditScore;
    PurposeOfLoan purposeOfLoan;
    Occupation occupation;
    Integer age;
    User user;

    public ProfileRequest toRequest() {
        return ProfileRequest.builder()
            .desiredLimit(desiredLimit)
            .creditScore(creditScore)
            .purposeOfLoan(purposeOfLoan)
            .occupation(occupation)
            .age(age)
            .build();
    }

    public RecommendLoanProductProfileDto(Profile profile) {
        desiredLimit = profile.getDesiredLoanAmount();
        creditScore = profile.getCreditScore();
        purposeOfLoan = profile.getPurposeOfLoan();
        occupation = profile.getOccupation();
        age = profile.getUser().getAge();
        profileId = profile.getId();
        user = profile.getUser();
    }
}
