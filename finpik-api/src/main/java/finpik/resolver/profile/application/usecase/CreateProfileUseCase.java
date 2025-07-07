package finpik.resolver.profile.application.usecase;

import finpik.resolver.profile.application.dto.CreateProfileUseCaseDto;
import finpik.resolver.profile.application.dto.ProfileResultDto;

public interface CreateProfileUseCase {
    ProfileResultDto execute(CreateProfileUseCaseDto profileDto);
}
