package finpik.resolver.profile.application.usecase;

import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.resolver.profile.application.dto.UpdateProfileColorUseCaseDto;

public interface UpdateProfileColorUseCase {
    ProfileDto execute(UpdateProfileColorUseCaseDto dto);
}
