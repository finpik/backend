package com.loanpick.profile.resolver;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.resolver.input.CreateProfileInput;
import com.loanpick.profile.resolver.result.ProfileResult;
import com.loanpick.profile.service.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProfileResolver implements ProfileApi {
    private final ProfileService profileService;

    @Override
    @MutationMapping
    public ProfileResult createProfile(@Argument @Valid CreateProfileInput input) {
        Profile profile = profileService.createProfile(input.toDto());

        return ProfileResult.builder().purposeOfLoan(profile.getPurposeOfLoan())
                .creditGradeStatus(profile.getCreditGradeStatus())
                .loanProductUsageCount(profile.getLoanProductUsageCount())
                .totalLoanUsageAmount(profile.getTotalLoanUsageAmount())
                .desiredLoanAmount(profile.getDesiredLoanAmount()).employmentStatus(profile.getEmploymentStatus())
                .build();
    }

    @Override
    public ProfileResult profileByUserId(@Argument long id) {
        return null;
    }
}
