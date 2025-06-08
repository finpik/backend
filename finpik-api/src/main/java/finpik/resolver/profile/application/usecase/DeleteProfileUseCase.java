package finpik.resolver.profile.application.usecase;

import finpik.resolver.profile.application.dto.ProfileDto;

import java.util.List;

public interface DeleteProfileUseCase {
    List<ProfileDto> execute(Long profileId, Long userId);
}
