package com.loanpick.profile.service;

import java.util.List;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.service.dto.CreateProfileDto;
import com.loanpick.user.entity.User;

public interface ProfileService {

    Profile createProfile(CreateProfileDto dto);

    List<Profile> getAllProfiles(User user);
}
