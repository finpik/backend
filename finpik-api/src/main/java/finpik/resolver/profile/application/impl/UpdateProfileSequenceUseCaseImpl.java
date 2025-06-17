package finpik.resolver.profile.application.impl;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.profile.entity.ProfileList;
import finpik.repository.profile.ProfileRepository;
import finpik.resolver.profile.application.usecase.UpdateProfileSequenceUseCase;
import finpik.resolver.profile.application.dto.ProfileResultDto;
import finpik.resolver.profile.application.dto.UpdateProfileSequenceUseCaseDto;
import finpik.user.entity.User;
import finpik.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static finpik.util.Values.PROFILE_LIMIT_NUMBER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UpdateProfileSequenceUseCaseImpl implements UpdateProfileSequenceUseCase {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public List<ProfileResultDto> execute(List<UpdateProfileSequenceUseCaseDto> dtos, Long userId) {
        validateDuplicateSequence(dtos);
        dtos.forEach(dto -> validateDtoCountLimit(dto.seq()));

        Map<Long, Integer> idSeqMap = dtos.stream()
            .collect(Collectors.toMap(
                UpdateProfileSequenceUseCaseDto::id,
                UpdateProfileSequenceUseCaseDto::seq
            ));

        ProfileList foundProfileList = profileRepository
            .findAllById(dtos.stream().map(UpdateProfileSequenceUseCaseDto::id).toList());

        User user = findUserById(userId);

        ProfileList changedProfileList = foundProfileList
            .validateAllProfilesBelongTo(user)
            .validateAllProfilesHaveSequence(idSeqMap)
            .updateSequences(idSeqMap);

        return profileRepository.updateAll(changedProfileList).getProfiles()
            .stream().map(ProfileResultDto::new).toList();
    }

    private void validateDuplicateSequence(List<UpdateProfileSequenceUseCaseDto> dtos) {
        Set<Integer> seenSeqSet = new HashSet<>();
        Set<Long> seenIdSet = new HashSet<>();

        for (UpdateProfileSequenceUseCaseDto dto : dtos) {
            if (!seenIdSet.add(dto.id())) {
                throw new BusinessException(ErrorCode.DUPLICATE_UPDATE_PROFILE_ID);
            }

            if (!seenSeqSet.add(dto.seq())) {
                throw new BusinessException(ErrorCode.DUPLICATE_PROFILE_SEQ);
            }
        }
    }

    private void validateDtoCountLimit(int count) {
        if (isNotLessThanLimit(count)) {
            log.error("[ProfileService] - limit reached");
            throw new BusinessException(ErrorCode.EXCEEDING_PROFILE_COUNT_LIMIT);
        }
    }

    private boolean isNotLessThanLimit(int count) {
        return count >= PROFILE_LIMIT_NUMBER;
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
    }
}
