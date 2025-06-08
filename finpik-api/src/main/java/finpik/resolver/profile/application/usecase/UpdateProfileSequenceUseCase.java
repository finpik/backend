package finpik.resolver.profile.application.usecase;

import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.resolver.profile.application.dto.UpdateProfileSequenceUseCaseDto;

import java.util.List;

public interface UpdateProfileSequenceUseCase {
    List<ProfileDto> execute(List<UpdateProfileSequenceUseCaseDto> dtos, Long userId);
}
