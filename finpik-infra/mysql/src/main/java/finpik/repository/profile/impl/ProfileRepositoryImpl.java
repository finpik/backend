package finpik.repository.profile.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import finpik.entity.Profile;
import finpik.entity.ProfileList;
import finpik.entity.User;
import finpik.jpa.repository.profile.ProfileEntityJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import finpik.entity.profile.ProfileEntity;
import finpik.entity.user.UserEntity;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.jpa.repository.user.UserEntityJpaRepository;
import finpik.repository.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepository {
    private final ProfileEntityJpaRepository profileEntityJpaRepository;
    private final UserEntityJpaRepository userEntityJpaRepository;

    @Override
    @Transactional
    public Profile save(Profile profile) {
        UserEntity userEntity = userEntityJpaRepository.findById(profile.getUser().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        ProfileEntity entity = ProfileEntity.from(profile, userEntity);

        return profileEntityJpaRepository.save(entity).toDomain();
    }

    @Override
    public Profile saveNewAndUpdateExistProfileList(ProfileList profileList) {
        UserEntity userEntity = userEntityJpaRepository
            .findById(profileList.getProfileList().getFirst().getUser().getId())
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        List<Long> existProfileIdList = profileList.profileList().stream().map(Profile::getId)
            .filter(Objects::nonNull).toList();

        List<ProfileEntity> exsitProfileEntityList =
            new ArrayList<>(profileEntityJpaRepository.findAllById(existProfileIdList));

        updateFields(exsitProfileEntityList, profileList.profileList());

        exsitProfileEntityList.add(ProfileEntity.from(profileList.getProfileList().getFirst(), userEntity));

        ProfileEntity profileEntity = profileEntityJpaRepository.saveAll(exsitProfileEntityList)
            .stream().filter(entity -> entity.getSeq() == 0).toList().getFirst();

        return profileEntity.toDomain();
    }


    @Override
    public Profile update(Profile profile) {
        ProfileEntity entity = profileEntityJpaRepository.findById(profile.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PROFILE));

        entity.updateAllFields(profile);

        return entity.toDomain();
    }

    @Override
    public ProfileList updateAll(ProfileList profileList) {
        List<ProfileEntity> profileEntityList = profileEntityJpaRepository
                .findAllById(profileList.getIds());

        updateFields(profileEntityList, profileList.getProfileList());

        return new ProfileList(profileList.getProfileList());
    }

    private void updateFields(
        List<ProfileEntity> profileEntityList,
        List<Profile> profileList
    ) {
        Map<Long, Profile> idProfileMap = profileList.stream()
            .filter(profile -> profile.getId() != null)
            .collect(Collectors.toMap(Profile::getId, profile -> profile));

        profileEntityList.forEach(profileEntity -> {
            Profile profile = idProfileMap.get(profileEntity.getId());

            if (profile != null) {
                profileEntity.updateAllFields(profile);
            }
        });

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Profile> findById(Long id) {
        return profileEntityJpaRepository.findById(id).map(ProfileEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileList findByUser(User user) {
        UserEntity userEntity = userEntityJpaRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        return new ProfileList(
            profileEntityJpaRepository.findByUser(userEntity).stream().map(ProfileEntity::toDomain).toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileList findAllById(List<Long> ids) {
        return new ProfileList(
            profileEntityJpaRepository.findAllById(ids).stream().map(ProfileEntity::toDomain).toList()
        );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        profileEntityJpaRepository.deleteById(id);
    }

    @Override
    public void deleteAllById(List<Long> idList) {
        profileEntityJpaRepository.deleteAllById(idList);
    }
}
