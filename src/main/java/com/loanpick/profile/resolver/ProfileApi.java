package com.loanpick.profile.resolver;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;

import com.loanpick.profile.resolver.input.CreateProfileInput;
import com.loanpick.profile.resolver.input.UpdateProfileInput;
import com.loanpick.profile.resolver.input.UpdateProfileSequenceInput;
import com.loanpick.profile.resolver.result.ProfileResult;
import com.loanpick.user.entity.User;

import jakarta.validation.Valid;

public interface ProfileApi {
    ProfileResult createProfile(@Argument @Valid CreateProfileInput input, User user);

    List<ProfileResult> profileByUserId(User user);

    ProfileResult updateProfile(@Argument @Valid UpdateProfileInput input);

    List<ProfileResult> updateProfileSequence(@Argument @Valid List<UpdateProfileSequenceInput> input, User user);
}
