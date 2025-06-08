package finpik.profile.entity;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.user.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static finpik.util.Preconditions.require;

@Slf4j
public record ProfileList(
    List<Profile> profileList
) {
    private static final int PROFILE_LIMIT_NUMBER = 4;

    public ProfileList {
        require(profileList, "프로파일들이 없습니다.");
    }

    public void balanceSequence(int startSeq) {
        if (profileList.isEmpty()) {
            return;
        }

        AtomicInteger seq = new AtomicInteger(startSeq);

        profileList.stream()
            .sorted(Comparator.comparing(Profile::getSeq))
            .forEach(profile -> profile.updateSequence(seq.getAndIncrement()));
    }

    public void validateProfileCountLimit() {
        if (isNotLessThanLimit(profileList.size())) {
            log.error("[ProfileService] - limit reached");
            throw new BusinessException(ErrorCode.EXCEEDING_PROFILE_COUNT_LIMIT);
        }
    }

    private boolean isNotLessThanLimit(int count) {
        return count >= PROFILE_LIMIT_NUMBER;
    }

    public List<Profile> getProfiles() {
        return Collections.unmodifiableList(profileList);
    }

    public ProfileList deleteProfile(Long deletedId) {
        return new ProfileList(profileList.stream().filter(profile ->
            !profile.getId().equals(deletedId)
        ).toList());
    }

    public int size() {
        return profileList.size();
    }

    public List<Long> getIds() {
        return profileList.stream().map(Profile::getId).toList();
    }

    public ProfileList validateAllProfilesBelongTo(User owner) {
        for (Profile profile : profileList) {
            if (!profile.getUser().getId().equals(owner.getId())) {
                throw new BusinessException(ErrorCode.NOT_PROFILE_OWNER);
            }
        }
        return this;
    }

    public ProfileList validateAllProfilesHaveSequence(Map<Long, Integer> idSeqMap) {
        for (Profile profile : profileList) {
            if (!idSeqMap.containsKey(profile.getId())) {
                throw new BusinessException(ErrorCode.PROFILE_SEQUENCE_CANNOT_BE_NULL);
            }
        }

        return this;
    }

    public ProfileList updateSequences(Map<Long, Integer> idSeqMap) {
        for (Profile profile : profileList) {
            Integer seq = idSeqMap.get(profile.getId());
            profile.updateSequence(seq);
        }

        return new ProfileList(profileList);
    }
}
