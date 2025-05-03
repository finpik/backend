package com.loanpick.profile.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanpick.error.enums.ErrorCode;
import com.loanpick.error.exception.BusinessException;
import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.repository.ProfileRepository;
import com.loanpick.profile.service.dto.CreateProfileDto;
import com.loanpick.profile.service.dto.UpdateProfileColorDto;
import com.loanpick.profile.service.dto.UpdateProfileDto;
import com.loanpick.profile.service.dto.UpdateProfileSequenceDto;
import com.loanpick.user.entity.User;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final EntityManager entityManager;
    private static final int PROFILE_LIMIT_NUMBER = 4;
    private static final int AVOID_SEQUENCE_NUMBER = 5;

    @Override
    @Transactional
    public Profile createProfile(CreateProfileDto dto) {
        Profile entity = dto.toEntity();

        List<Profile> profileList = getProfileListBy(dto.user());
        validateProfileCountLimit(profileList.size());

        Map<Long, Integer> previousIdSeqMap = memoryPreviousSequenceMap(profileList);
        List<Profile> advoidedProfileList = avoidUniqueIndex(profileList);

        int startSeq = 1;
        balanceProfileSequence(advoidedProfileList, previousIdSeqMap, startSeq);

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
        validateDuplicate(dtos);
        dtos.forEach(dto -> validateProfileCountLimit(dto.seq()));

        Map<Long, Integer> idSeqMap = dtos.stream()
                .collect(Collectors.toMap(UpdateProfileSequenceDto::id, UpdateProfileSequenceDto::seq));

        List<Profile> foundProfileList = profileRepository
                .findAllById(dtos.stream().map(UpdateProfileSequenceDto::id).toList());

        foundProfileList.forEach(profile -> validateProfileOwner(profile, user));

        List<Profile> avoidedProfileList = avoidUniqueIndex(foundProfileList);

        avoidedProfileList
                .forEach(avoidedProfile -> avoidedProfile.updateSequence(idSeqMap.get(avoidedProfile.getId())));

        return profileRepository.saveAll(avoidedProfileList);
    }

    @Override
    @Transactional
    public Profile updateProfileColor(UpdateProfileColorDto dto) {
        Profile profile = findProfileBy(dto.id());

        profile.changeProfileColor(dto.profileColor());

        return profile;
    }

    @Override
    @Transactional
    public List<Profile> deleteProfile(Long deletedId, User user) {
        List<Profile> foundProfileList = findProfileListBy(user);

        foundProfileList.stream().filter(profile -> profile.getId().equals(deletedId)).findFirst()
                .ifPresent(profile -> validateProfileOwner(profile, user));

        profileRepository.deleteById(deletedId);

        List<Profile> profileList = foundProfileList.stream().filter(profile -> !profile.getId().equals(deletedId))
                .toList();

        Map<Long, Integer> idSeqMap = memoryPreviousSequenceMap(profileList);
        avoidUniqueIndex(profileList);

        int startSeq = 0;
        balanceProfileSequence(profileList, idSeqMap, startSeq);

        return profileList;
    }

    @Override
    public Profile getProfileBy(Long id, User user) {
        Profile foundProfile = findProfileBy(id);
        validateProfileOwner(foundProfile, user);

        return foundProfile;
    }

    private void validateProfileCountLimit(int count) {
        if (isNotLessThanLimit(count)) {
            log.error("[ProfileService] - limit reached");
            throw new BusinessException(ErrorCode.EXCEEDING_PROFILE_COUNT_LIMIT);
        }
    }

    private void balanceProfileSequence(List<Profile> profileList, Map<Long, Integer> previousIdSeqMap, int startSeq) {
        if (profileList.isEmpty()) {
            return;
        }

        for (Profile profile : profileList) {
            Integer seq = previousIdSeqMap.get(profile.getId());

            profile.updateSequence(seq);
        }

        List<Profile> sortedProfileList = profileList.stream().sorted(Comparator.comparing(Profile::getSeq)).toList();

        for (Profile profile : sortedProfileList) {
            profile.updateSequence(startSeq);
            startSeq++;
        }

        profileRepository.saveAll(sortedProfileList);
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

    private void validateDuplicate(List<UpdateProfileSequenceDto> dtos) {
        Set<Integer> seenSeqSet = new HashSet<>();
        Set<Long> seenIdSet = new HashSet<>();

        for (UpdateProfileSequenceDto dto : dtos) {
            if (!seenIdSet.add(dto.id())) {
                throw new BusinessException(ErrorCode.DUPLICATE_UPDATE_PROFILE_ID);
            }

            if (!seenSeqSet.add(dto.seq())) {
                throw new BusinessException(ErrorCode.DUPLICATE_PROFILE_SEQ);
            }
        }
    }

    private List<Profile> avoidUniqueIndex(List<Profile> profileList) {
        for (Profile profile : profileList) {
            profile.updateSequence(profile.getSeq() - AVOID_SEQUENCE_NUMBER);
        }

        List<Profile> advoidedProfileList = profileRepository.saveAll(profileList);
        entityManager.flush();

        return advoidedProfileList;
    }

    private Map<Long, Integer> memoryPreviousSequenceMap(List<Profile> profileList) {
        Map<Long, Integer> tempIdSeqMap = new HashMap<>();
        for (Profile profile : profileList) {
            tempIdSeqMap.put(profile.getId(), profile.getSeq());
        }

        return tempIdSeqMap;
    }
}
