package com.loanpick.profile.resolver;

import com.loanpick.profile.resolver.input.CreateProfileInput;
import com.loanpick.profile.resolver.result.ProfileResult;

public interface ProfileApi {
    ProfileResult createProfile(CreateProfileInput input);

    ProfileResult profileByUserId(long id);
}
