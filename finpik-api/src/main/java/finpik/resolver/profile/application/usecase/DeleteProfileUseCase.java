package finpik.resolver.profile.application.usecase;

import finpik.resolver.profile.application.dto.ProfileResultDto;

import java.util.List;

public interface DeleteProfileUseCase {
    List<ProfileResultDto> execute(Long profileId, Long userId);
}
