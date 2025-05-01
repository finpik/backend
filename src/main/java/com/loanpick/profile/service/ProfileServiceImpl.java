package com.loanpick.profile.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanpick.error.enums.ErrorCode;
import com.loanpick.error.exception.BusinessException;
import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.repository.ProfileRepository;
import com.loanpick.profile.service.dto.CreateProfileDto;
import com.loanpick.profile.service.dto.UpdateProfileDto;
import com.loanpick.profile.service.dto.UpdateProfileSequenceDto;
import com.loanpick.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private static final int PROFILE_LIMIT_NUMBER = 4;

    @Override
    @Transactional
    public Profile createProfile(CreateProfileDto dto) {
        Profile entity = dto.toEntity();
        List<Profile> profileList = getProfileListBy(dto.user());
        validateProfileCountLimit(profileList.size());
        balanceProfileSequence(profileList);

        return profileRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getProfileListBy(User user) {
        return findProfileListBy(user);
    }

    @Override
    @Transactional
    public Profile updateProfile(UpdateProfileDto dto) {
        Profile savedEntity = findProfileBy(dto.id());
        Profile changedEntity = dto.toEntity();

        savedEntity.updateProfile(changedEntity);

        return savedEntity;
    }

    @Override
    @Transactional
    public List<Profile> updateProfileSequence(List<UpdateProfileSequenceDto> dtos, User user) {
        Map<Long, Profile> foundProfileMap = profileRepository
                .findAllById(dtos.stream().map(UpdateProfileSequenceDto::id).toList()).stream()
                .collect(Collectors.toMap(Profile::getId, profile -> profile));

        dtos.forEach(dto -> {
            Profile profile = foundProfileMap.get(dto.id());
            validateProfileOwner(profile, user);

            validateProfileCountLimit(dto.seq());
            profile.updateSequence(dto.seq());
        });

        return foundProfileMap.values().stream().toList();
    }

    private void validateProfileCountLimit(int count) {
        if (isNotLessThanLimit(count)) {
            log.error("[ProfileService] - limit reached");
            throw new BusinessException(ErrorCode.EXCEEDING_PROFILE_COUNT_LIMIT);
        }
    }

    private void balanceProfileSequence(List<Profile> profileList) {
        if (profileList.isEmpty())
            return;

        profileList.forEach(Profile::balanceSequence);
    }

    private List<Profile> findProfileListBy(User user) {
        return profileRepository.findByUser(user);
    }

    private boolean isNotLessThanLimit(int count) {
        return count >= PROFILE_LIMIT_NUMBER;
    }

    private Profile findProfileBy(long id) {
        return profileRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PROFILE));
    }

    private void validateProfileOwner(Profile profile, User user) {
        if (profile.getUser().equals(user)) {
            throw new BusinessException(ErrorCode.NOT_PROFILE_OWNER);
        }
    }
}
