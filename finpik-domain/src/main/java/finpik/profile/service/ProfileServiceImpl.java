package finpik.profile.service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.profile.entity.Profile;
import finpik.profile.repository.ProfileRepository;
import finpik.profile.service.dto.CreateProfileDto;
import finpik.profile.service.dto.UpdateProfileColorDto;
import finpik.profile.service.dto.UpdateProfileDto;
import finpik.profile.service.dto.UpdateProfileSequenceDto;
import finpik.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private static final int PROFILE_LIMIT_NUMBER = 4;

    @Override
    public Profile createProfile(CreateProfileDto dto) {
        Profile entity = dto.toEntity();

        List<Profile> profileList = findProfileListBy(dto.user());
        validateProfileCountLimit(profileList.size());

        int startSeq = 1;
        balanceProfileSequence(profileList, startSeq);

        return profileRepository.save(entity);
    }

    @Override
    public List<Profile> getProfileListBy(User user) {
        return findProfileListBy(user);
    }

    @Override
    public Profile updateProfile(UpdateProfileDto dto) {
        Profile profile = findProfileBy(dto.id());
        Profile changedProfile = dto.toEntity();

        profile.updateProfile(changedProfile);

        return profileRepository.update(profile);
    }

    @Override
    public List<Profile> updateProfileSequence(List<UpdateProfileSequenceDto> dtos, User user) {
        validateDuplicateSequence(dtos);
        dtos.forEach(dto -> validateProfileCountLimit(dto.seq()));

        Map<Long, Integer> idSeqMap = dtos.stream()
                .collect(Collectors.toMap(UpdateProfileSequenceDto::id, UpdateProfileSequenceDto::seq));

        List<Profile> foundProfileList = profileRepository
                .findAllById(dtos.stream().map(UpdateProfileSequenceDto::id).toList());

        foundProfileList.forEach(profile -> {
            validateProfileOwner(profile, user);
            Integer seq = idSeqMap.get(profile.getId());

            if (seq == null) {
                throw new BusinessException(ErrorCode.PROFILE_SEQUENCE_CANNOT_BE_NULL);
            }

            profile.updateSequence(seq);
        });

        return profileRepository.updateAll(foundProfileList);
    }

    @Override
    public Profile updateProfileColor(UpdateProfileColorDto dto) {
        Profile profile = findProfileBy(dto.id());

        profile.changeProfileColor(dto.profileColor());

        return profileRepository.save(profile);
    }

    @Override
    public List<Profile> deleteProfile(Long deletedId, User user) {
        List<Profile> foundProfileList = findProfileListBy(user);

        foundProfileList.stream().filter(profile -> profile.getId().equals(deletedId)).findFirst()
                .ifPresent(profile -> validateProfileOwner(profile, user));

        profileRepository.deleteById(deletedId);

        List<Profile> profileList = foundProfileList.stream().filter(profile -> !profile.getId().equals(deletedId))
                .toList();

        int startSeq = 0;
        balanceProfileSequence(profileList, startSeq);

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

    private void balanceProfileSequence(List<Profile> profileList, int startSeq) {
        if (profileList.isEmpty()) {
            return;
        }

        List<Profile> sortedProfileList = profileList.stream().sorted(Comparator.comparing(Profile::getSeq)).toList();

        for (Profile profile : sortedProfileList) {
            profile.updateSequence(startSeq);
            startSeq++;
        }
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

    private void validateDuplicateSequence(List<UpdateProfileSequenceDto> dtos) {
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
}
