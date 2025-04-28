package com.loanpick.profile.service;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.service.dto.CreateProfileDto;

public interface ProfileService {

    Profile createProfile(CreateProfileDto dto);
}
