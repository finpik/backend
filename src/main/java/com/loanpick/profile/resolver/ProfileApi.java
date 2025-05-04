package com.loanpick.profile.resolver;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;

import com.loanpick.profile.resolver.input.CreateProfileInput;
import com.loanpick.profile.resolver.input.UpdateProfileColorInput;
import com.loanpick.profile.resolver.input.UpdateProfileInput;
import com.loanpick.profile.resolver.input.UpdateProfileSequenceInput;
import com.loanpick.profile.resolver.result.ProfileResult;
import com.loanpick.user.entity.User;

import jakarta.validation.Valid;

public interface ProfileApi {
    ProfileResult createProfile(@Argument @Valid CreateProfileInput input,
            @ContextValue(value = "user", required = false) User userInput);

    List<ProfileResult> profileByUser(@ContextValue(value = "user", required = false) User userInput);

    ProfileResult profileById(@Argument Long id, @ContextValue(value = "user", required = false) User userInput);

    ProfileResult updateProfile(@Argument @Valid UpdateProfileInput input,
            @ContextValue(value = "user", required = false) User userInput);

    List<ProfileResult> updateProfileSequence(@Argument @Valid List<UpdateProfileSequenceInput> input,
            @ContextValue(value = "user", required = false) User userInput);

    ProfileResult updateProfileColor(@Argument @Valid UpdateProfileColorInput input,
            @ContextValue(value = "user", required = false) User userInput);

    List<ProfileResult> deleteProfile(@Argument Long deletedId,
            @ContextValue(value = "user", required = false) User userInput);
}
