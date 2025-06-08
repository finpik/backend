package finpik.resolver.profile.application.usecase;

import finpik.resolver.profile.application.dto.ProfileDto;

public interface GetProfileByIdUseCase {
    ProfileDto execute(Long id, Long userId);
}
