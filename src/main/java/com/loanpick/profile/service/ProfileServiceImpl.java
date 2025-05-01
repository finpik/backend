package com.loanpick.profile.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanpick.error.enums.ErrorCode;
import com.loanpick.error.exception.BusinessException;
import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.repository.ProfileRepository;
import com.loanpick.profile.service.dto.CreateProfileDto;
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
        validateProfileCountLimit(profileList);
        balanceProfileSequence(profileList);

        return profileRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getAllProfiles(User user) {
        return profileRepository.findByUser(user);
    }
    private void validateProfileCountLimit(List<Profile> profileList) {
        if (isNotLessThanLimit(profileList.size())) {
            log.error("[ProfileService] - limit reached");
            throw new BusinessException(ErrorCode.EXCEEDING_PROFILE_COUNT_LIMIT);
        }
    }

    private void balanceProfileSequence(List<Profile> profileList) {
        if (profileList.isEmpty())
            return;

        profileList.forEach(Profile::balanceSequence);
    }

    private List<Profile> getProfileListBy(User user) {
        return profileRepository.findByUser(user);
    }

    private boolean isNotLessThanLimit(int count) {
        return count >= PROFILE_LIMIT_NUMBER;
    }
}
