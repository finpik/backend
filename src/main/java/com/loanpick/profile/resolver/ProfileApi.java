package com.loanpick.profile.resolver;

import com.loanpick.profile.resolver.input.CreateProfileInput;
import com.loanpick.profile.resolver.result.ProfileResult;
import org.springframework.graphql.data.method.annotation.Argument;

public interface ProfileApi {
    ProfileResult createProfile(@Argument CreateProfileInput input);
    ProfileResult profileByUserId(@Argument long id);

}
