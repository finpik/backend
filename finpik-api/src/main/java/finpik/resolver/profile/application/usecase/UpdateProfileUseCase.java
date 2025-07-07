package finpik.resolver.profile.application.usecase;

import finpik.resolver.profile.application.dto.ProfileResultDto;
import finpik.resolver.profile.application.dto.UpdateProfileUseCaseDto;

public interface UpdateProfileUseCase {
    ProfileResultDto execute(UpdateProfileUseCaseDto dto);
}
