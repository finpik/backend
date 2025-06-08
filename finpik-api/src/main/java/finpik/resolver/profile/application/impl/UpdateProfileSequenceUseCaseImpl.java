package finpik.resolver.profile.application.impl;

import finpik.profile.entity.Profile;
import finpik.profile.service.ProfileService;
import finpik.profile.service.dto.UpdateProfileSequenceDto;
import finpik.resolver.profile.application.usecase.UpdateProfileSequenceUseCase;
import finpik.resolver.profile.application.dto.ProfileDto;
import finpik.resolver.profile.application.dto.UpdateProfileSequenceUseCaseDto;
import finpik.user.entity.User;
import finpik.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateProfileSequenceUseCaseImpl implements UpdateProfileSequenceUseCase {
    private final ProfileService profileService;
    private final UserService userService;

    @Override
    public List<ProfileDto> execute(List<UpdateProfileSequenceUseCaseDto> dtos, Long userId) {
        User user = userService.findUserBy(userId);

        List<UpdateProfileSequenceDto> domainDtos = dtos.stream().map(UpdateProfileSequenceUseCaseDto::toDomainDto)
            .toList();

        List<Profile> profileList = profileService.updateProfileSequence(domainDtos, user);

        return profileList.stream().map(ProfileDto::new).toList();
    }
}
