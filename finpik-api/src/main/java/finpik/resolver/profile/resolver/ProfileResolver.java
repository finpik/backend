package finpik.resolver.profile.resolver;

import java.util.List;

import finpik.resolver.profile.application.usecase.CreateProfileUseCase;
import finpik.resolver.profile.application.usecase.DeleteProfileUseCase;
import finpik.resolver.profile.application.usecase.GetProfileByIdUseCase;
import finpik.resolver.profile.application.usecase.GetProfileListByUserUseCase;
import finpik.resolver.profile.application.usecase.UpdateProfileColorUseCase;
import finpik.resolver.profile.application.usecase.UpdateProfileSequenceUseCase;
import finpik.resolver.profile.application.usecase.UpdateProfileUseCase;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import finpik.resolver.profile.application.dto.ProfileResultDto;
import finpik.resolver.profile.application.dto.UpdateProfileSequenceUseCaseDto;
import finpik.resolver.profile.resolver.input.CreateProfileInput;
import finpik.resolver.profile.resolver.input.UpdateProfileColorInput;
import finpik.resolver.profile.resolver.input.UpdateProfileInput;
import finpik.resolver.profile.resolver.input.UpdateProfileSequenceInput;
import finpik.resolver.profile.resolver.result.ProfileResult;
import finpik.user.entity.User;
import finpik.util.UserUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProfileResolver implements ProfileApi {
    private final CreateProfileUseCase createProfileUseCase;
    private final DeleteProfileUseCase deleteProfileUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final UpdateProfileSequenceUseCase updateProfileSequenceUseCase;
    private final UpdateProfileColorUseCase updateProfileColorUseCase;
    private final GetProfileListByUserUseCase getProfileListByUserUseCase;
    private final GetProfileByIdUseCase getProfileByIdUseCase;

    @Override
    @MutationMapping
    public ProfileResult createProfile(@Argument @Valid CreateProfileInput input,
            @ContextValue(value = "user", required = false) User userInput) {
        User user = validateUser(userInput);

        ProfileResultDto profile = createProfileUseCase.execute(input.toDto(user.getId()));

        return ProfileResult.of(profile);
    }

    @Override
    @QueryMapping
    public List<ProfileResult> getProfilesByUser(@ContextValue(value = "user", required = false) User userInput) {
        User userDto = validateUser(userInput);

        List<ProfileResultDto> profileList = getProfileListByUserUseCase.execute(userDto.getId());

        return profileList.stream().map(ProfileResult::of).toList();
    }

    @Override
    @QueryMapping
    public ProfileResult getProfileById(
        @Argument Long profileId,
        @ContextValue(value = "user", required = false) User userInput
    ) {
        validateUser(userInput);

        ProfileResultDto profile = getProfileByIdUseCase.execute(profileId, userInput.getId());

        return ProfileResult.of(profile);
    }

    @Override
    @MutationMapping
    public ProfileResult updateProfile(@Argument @Valid UpdateProfileInput input,
            @ContextValue(value = "user", required = false) User userInput) {
        validateUser(userInput);

        ProfileResultDto profile = updateProfileUseCase.execute(input.toDto());

        return ProfileResult.of(profile);
    }

    @Override
    @MutationMapping
    public List<ProfileResult> updateProfileSequence(@Argument @Valid List<UpdateProfileSequenceInput> input,
            @ContextValue(value = "user", required = false) User userInput) {
        User user = validateUser(userInput);

        List<UpdateProfileSequenceUseCaseDto> dtos = input.stream().map(UpdateProfileSequenceInput::toDto).toList();

        List<ProfileResultDto> profileList = updateProfileSequenceUseCase.execute(dtos, user.getId());

        return profileList.stream().map(ProfileResult::of).toList();
    }

    @Override
    @MutationMapping
    public ProfileResult updateProfileColor(@Argument @Valid UpdateProfileColorInput input,
            @ContextValue(value = "user", required = false) User userInput) {
        validateUser(userInput);

        return ProfileResult.of(updateProfileColorUseCase.execute(input.toDto()));
    }

    @Override
    @MutationMapping
    public List<ProfileResult> deleteProfile(@Argument Long deletedId,
            @ContextValue(value = "user", required = false) User userInput) {
        User user = validateUser(userInput);

        List<ProfileResultDto> profileList = deleteProfileUseCase.execute(deletedId, user.getId());

        return profileList.stream().map(ProfileResult::of).toList();
    }

    public User validateUser(User userInput) {
        return UserUtil.require(userInput);
    }
}
