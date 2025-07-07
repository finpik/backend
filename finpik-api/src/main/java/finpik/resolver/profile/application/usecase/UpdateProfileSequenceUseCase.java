package finpik.resolver.profile.application.usecase;

import finpik.resolver.profile.application.dto.ProfileResultDto;
import finpik.resolver.profile.application.dto.UpdateProfileSequenceUseCaseDto;

import java.util.List;

public interface UpdateProfileSequenceUseCase {
    List<ProfileResultDto> execute(List<UpdateProfileSequenceUseCaseDto> dtos, Long userId);
}
