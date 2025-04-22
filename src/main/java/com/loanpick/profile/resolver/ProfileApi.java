package com.loanpick.profile.resolver;

import org.springframework.graphql.data.method.annotation.Argument;

import com.loanpick.profile.resolver.input.CreateProfileInput;
import com.loanpick.profile.resolver.result.ProfileResult;

public interface ProfileApi {
    ProfileResult createProfile(@Argument CreateProfileInput input);

    ProfileResult profileByUserId(@Argument long id);
}
