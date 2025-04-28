package com.loanpick.profile.service;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.repository.ProfileRepository;
import com.loanpick.profile.service.dto.CreateProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public Profile createProfile(CreateProfileDto dto) {
        Profile entity = dto.toEntity();

        return profileRepository.save(entity);
    }
}
