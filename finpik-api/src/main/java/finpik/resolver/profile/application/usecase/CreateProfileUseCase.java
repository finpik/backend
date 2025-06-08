package finpik.resolver.profile.application.usecase;

import finpik.resolver.profile.application.dto.CreateProfileUseCaseDto;
import finpik.resolver.profile.application.dto.ProfileDto;

public interface CreateProfileUseCase {
    ProfileDto execute(CreateProfileUseCaseDto profileDto);
}
