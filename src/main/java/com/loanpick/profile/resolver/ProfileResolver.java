package com.loanpick.profile.resolver;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.resolver.input.CreateProfileInput;
import com.loanpick.profile.resolver.input.UpdateProfileInput;
import com.loanpick.profile.resolver.input.UpdateProfileSequenceInput;
import com.loanpick.profile.resolver.result.ProfileResult;
import com.loanpick.profile.service.ProfileService;
import com.loanpick.profile.service.dto.UpdateProfileSequenceDto;
import com.loanpick.user.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProfileResolver implements ProfileApi {
    private final ProfileService profileService;

    @Override
    @MutationMapping
    public ProfileResult createProfile(@Argument @Valid CreateProfileInput input, User user) {
        Profile profile = profileService.createProfile(input.toDto(user));

        return ProfileResult.of(profile);
    }

    @Override
    public List<ProfileResult> profileByUserId(User user) {
        List<Profile> profileList = profileService.getAllProfiles(user);
    @Override
    public ProfileResult updateProfile(@Argument @Valid UpdateProfileInput input) {
        Profile profile = profileService.updateProfile(input.toDto());

        return ProfileResult.of(profile);
    }

        return profileList.stream().map(ProfileResult::of).toList();
    }
}
