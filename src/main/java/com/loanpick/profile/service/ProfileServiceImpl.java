package com.loanpick.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.repository.ProfileRepository;
import com.loanpick.profile.service.dto.CreateProfileDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private static final int PROFILE_LIMIT_NUMBER = 4;

    @Override
    @Transactional
    public Profile createProfile(CreateProfileDto dto) {
        Profile entity = dto.toEntity();
        List<Profile> profileList = profileRepository.findByUser(dto.user());
        validateProfileCountLimit(profileList);

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
    private boolean isNotLessThanLimit(int count) {
        return count >= PROFILE_LIMIT_NUMBER;
    }
}
