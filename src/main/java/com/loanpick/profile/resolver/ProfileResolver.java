package com.loanpick.profile.resolver;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.resolver.input.CreateProfileInput;
import com.loanpick.profile.resolver.input.UpdateProfileColorInput;
import com.loanpick.profile.resolver.input.UpdateProfileInput;
import com.loanpick.profile.resolver.input.UpdateProfileSequenceInput;
import com.loanpick.profile.resolver.result.ProfileResult;
import com.loanpick.profile.service.ProfileService;
import com.loanpick.profile.service.dto.UpdateProfileSequenceDto;
import com.loanpick.user.entity.User;
import com.loanpick.util.UserUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProfileResolver implements ProfileApi {
    private final ProfileService profileService;

    @Override
    @MutationMapping
    public ProfileResult createProfile(@Argument @Valid CreateProfileInput input,
            @ContextValue("user") User userInput) {
        User user = UserUtil.require(userInput);

        Profile profile = profileService.createProfile(input.toDto(user));

        return ProfileResult.of(profile);
    }

    @Override
    @QueryMapping
    public List<ProfileResult> profileByUser(@ContextValue("user") User userInput) {
        User user = UserUtil.require(userInput);

        List<Profile> profileList = profileService.getProfileListBy(user);

        return profileList.stream().map(ProfileResult::of).toList();
    }

    @Override
    @MutationMapping
    public ProfileResult updateProfile(@Argument @Valid UpdateProfileInput input) {
        Profile profile = profileService.updateProfile(input.toDto());

        return ProfileResult.of(profile);
    }

    @Override
    @MutationMapping
    public List<ProfileResult> updateProfileSequence(@Argument @Valid List<UpdateProfileSequenceInput> input,
            @ContextValue("user") User userInput) {
        User user = UserUtil.require(userInput);

        List<UpdateProfileSequenceDto> dtos = input.stream().map(UpdateProfileSequenceInput::toDto).toList();
        List<Profile> profileList = profileService.updateProfileSequence(dtos, user);

        return profileList.stream().map(ProfileResult::of).toList();
    }

    @Override
    @MutationMapping
    public ProfileResult updateProfileColor(@Argument @Valid UpdateProfileColorInput input,
            @ContextValue("user") User userInput) {
        UserUtil.require(userInput);

        return ProfileResult.of(profileService.updateProfileColor(input.toDto()));
    }
}
