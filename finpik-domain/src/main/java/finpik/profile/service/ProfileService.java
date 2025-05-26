package finpik.profile.service;

import java.util.List;

import finpik.profile.entity.Profile;
import finpik.profile.service.dto.CreateProfileDto;
import finpik.profile.service.dto.UpdateProfileColorDto;
import finpik.profile.service.dto.UpdateProfileDto;
import finpik.profile.service.dto.UpdateProfileSequenceDto;
import finpik.user.entity.User;

public interface ProfileService {

    Profile createProfile(CreateProfileDto dto);

    List<Profile> getProfileListBy(User user);

    Profile updateProfile(UpdateProfileDto dto);

    List<Profile> updateProfileSequence(List<UpdateProfileSequenceDto> dto, User user);

    Profile updateProfileColor(UpdateProfileColorDto dto);

    List<Profile> deleteProfile(Long deletedId, User user);

    Profile getProfileBy(Long id, User user);
}
