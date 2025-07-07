package finpik.resolver.profile.application.usecase;

import finpik.resolver.profile.application.dto.ProfileResultDto;
import finpik.resolver.profile.application.dto.UpdateProfileColorUseCaseDto;

public interface UpdateProfileColorUseCase {
    ProfileResultDto execute(UpdateProfileColorUseCaseDto dto);
}
