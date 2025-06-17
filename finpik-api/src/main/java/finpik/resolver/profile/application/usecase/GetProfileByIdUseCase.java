package finpik.resolver.profile.application.usecase;

import finpik.resolver.profile.application.dto.ProfileResultDto;

public interface GetProfileByIdUseCase {
    ProfileResultDto execute(Long id, Long userId);
}
