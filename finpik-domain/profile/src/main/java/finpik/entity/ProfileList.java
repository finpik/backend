package finpik.entity;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
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
    private static final int START_SEQ_ADD = 1;
    private static final int START_SEQ_DELETE = 0;

    public ProfileList {
        require(profileList, "프로파일 리스트가 Null입니다.");
    }

    public ProfileList addProfile(Profile newProfile) {
        validateProfileCountLimit(START_SEQ_ADD + profileList.size());

        balanceSequence(START_SEQ_ADD);

        List<Profile> mutableProfileList = new ArrayList<>(profileList);
        mutableProfileList.add(newProfile);

        return new ProfileList(mutableProfileList.stream().sorted(Comparator.comparing(Profile::getSeq)).toList());
    }

    private void balanceSequence(int startSeq) {
        if (profileList.isEmpty()) {
            return;
        }

        validateProfileCountLimit(startSeq);

        AtomicInteger seq = new AtomicInteger(startSeq);

        profileList.stream()
            .sorted(Comparator.comparing(Profile::getSeq))
            .forEach(profile -> profile.updateSequence(seq.getAndIncrement()));
    }

    private void validateProfileCountLimit(int profilesCount) {
        if (isNotLessThanLimit(profilesCount)) {
            throw new BusinessException(ErrorCode.EXCEEDING_PROFILE_COUNT_LIMIT);
        }
    }

    private boolean isNotLessThanLimit(int count) {
        return count > PROFILE_LIMIT_NUMBER;
    }

    public List<Profile> getProfileList() {
        return Collections.unmodifiableList(profileList);
    }

    public ProfileList deleteProfile(Long deletedId) {
        ProfileList filteredProfileList = new ProfileList(profileList.stream().filter(profile ->
            !profile.getId().equals(deletedId)
        ).toList());

        filteredProfileList.balanceSequence(START_SEQ_DELETE);

        return filteredProfileList;
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
