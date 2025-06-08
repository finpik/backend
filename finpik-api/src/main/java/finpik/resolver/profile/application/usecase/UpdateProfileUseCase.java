package finpik.resolver.profile.application.usecase;

import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.resolver.profile.application.dto.UpdateProfileUseCaseDto;

public interface UpdateProfileUseCase {
    ProfileDto execute(UpdateProfileUseCaseDto dto);
}
