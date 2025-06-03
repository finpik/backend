package finpik.jpa.repository.profile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import finpik.entity.profile.ProfileEntity;
import finpik.entity.user.UserEntity;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.jpa.repository.user.UserEntityJpaRepository;
import finpik.profile.entity.Profile;
import finpik.profile.repository.ProfileRepository;
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
    public List<Profile> updateAll(List<Profile> profileList) {
        List<ProfileEntity> profileEntityList = profileEntityJpaRepository
                .findAllById(profileList.stream().map(Profile::getId).toList());

        Map<Long, Profile> idProfileMap = profileList.stream()
                .collect(Collectors.toMap(Profile::getId, profile -> profile));

        profileEntityList.forEach(profileEntity -> {
            Profile profile = idProfileMap.get(profileEntity.getUser().getId());

            if (profile != null) {
                profileEntity.updateAllFields(profile);
            }
        });

        return idProfileMap.values().stream().toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Profile> findById(Long id) {
        return profileEntityJpaRepository.findById(id).map(ProfileEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> findByUser(User user) {
        UserEntity userEntity = userEntityJpaRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        return profileEntityJpaRepository.findByUser(userEntity).stream().map(ProfileEntity::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> findAllById(List<Long> ids) {
        return profileEntityJpaRepository.findAllById(ids).stream().map(ProfileEntity::toDomain).toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        profileEntityJpaRepository.deleteById(id);
    }
}
