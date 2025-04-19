package com.loanpick.profile.resolver;

import com.loanpick.profile.resolver.input.CreateProfileInput;
import com.loanpick.profile.resolver.result.ProfileResult;
import org.springframework.stereotype.Controller;

@Controller
public class ProfileResolver implements ProfileApi {

    @Override
    public ProfileResult createProfile(CreateProfileInput input) {

        return null;
    }

    @Override
    public ProfileResult profileByUserId(long id) {
        return null;
    }
}
