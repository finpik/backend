package com.loanpick.profile.resolver;

import org.springframework.stereotype.Controller;

import com.loanpick.profile.resolver.input.CreateProfileInput;
import com.loanpick.profile.resolver.result.ProfileResult;

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
