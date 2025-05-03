package com.loanpick.profile.service;

import java.util.List;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.service.dto.CreateProfileDto;
import com.loanpick.profile.service.dto.UpdateProfileColorDto;
import com.loanpick.profile.service.dto.UpdateProfileDto;
import com.loanpick.profile.service.dto.UpdateProfileSequenceDto;
import com.loanpick.user.entity.User;

public interface ProfileService {

    Profile createProfile(CreateProfileDto dto);

    List<Profile> getProfileListBy(User user);

    Profile updateProfile(UpdateProfileDto dto);

    List<Profile> updateProfileSequence(List<UpdateProfileSequenceDto> dto, User user);

    Profile updateProfileColor(UpdateProfileColorDto dto);

    List<Profile> deleteProfile(Long deletedId, User user);

    Profile getProfileBy(Long id, User user);
}
