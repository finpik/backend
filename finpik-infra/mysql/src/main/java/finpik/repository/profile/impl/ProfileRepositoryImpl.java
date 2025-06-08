package finpik.repository.profile.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import finpik.jpa.repository.profile.ProfileEntityJpaRepository;
import finpik.profile.entity.ProfileList;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import finpik.entity.profile.ProfileEntity;
import finpik.entity.user.UserEntity;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.jpa.repository.user.UserEntityJpaRepository;
import finpik.profile.entity.Profile;
import finpik.repository.profile.ProfileRepository;
import finpik.user.entity.User;
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

        Map<Long, Profile> idProfileMap = profileList.getProfiles().stream()
                .collect(Collectors.toMap(Profile::getId, profile -> profile));

        profileEntityList.forEach(profileEntity -> {
            Profile profile = idProfileMap.get(profileEntity.getUser().getId());

            if (profile != null) {
                profileEntity.updateAllFields(profile);
            }
        });

        return new ProfileList(idProfileMap.values().stream().toList());
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
}
