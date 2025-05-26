package finpik.resolver.profile.resolver;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import finpik.resolver.profile.application.ProfileCommandService;
import finpik.resolver.profile.application.ProfileQueryService;
import finpik.resolver.profile.application.dto.ProfileDto;
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
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    @Override
    @MutationMapping
    public ProfileResult createProfile(@Argument @Valid CreateProfileInput input,
            @ContextValue(value = "user", required = false) User userInput) {
        User user = validateUser(userInput);

        ProfileDto profile = profileCommandService.createProfile(input.toDto(user.getId()));

        return ProfileResult.of(profile);
    }

    @Override
    @QueryMapping
    public List<ProfileResult> profileByUser(@ContextValue(value = "user", required = false) User userInput) {
        User userDto = validateUser(userInput);

        List<ProfileDto> profileList = profileQueryService.getProfileListByUser(userDto.getId());

        return profileList.stream().map(ProfileResult::of).toList();
    }

    @Override
    @QueryMapping
    public ProfileResult profileById(@Argument Long id,
            @ContextValue(value = "user", required = false) User userInput) {
        validateUser(userInput);

        ProfileDto profile = profileQueryService.getProfileById(id, userInput.getId());

        return ProfileResult.of(profile);
    }

    @Override
    @MutationMapping
    public ProfileResult updateProfile(@Argument @Valid UpdateProfileInput input,
            @ContextValue(value = "user", required = false) User userInput) {
        validateUser(userInput);

        ProfileDto profile = profileCommandService.updateProfile(input.toDto());

        return ProfileResult.of(profile);
    }

    @Override
    @MutationMapping
    public List<ProfileResult> updateProfileSequence(@Argument @Valid List<UpdateProfileSequenceInput> input,
            @ContextValue(value = "user", required = false) User userInput) {
        User user = validateUser(userInput);

        List<UpdateProfileSequenceUseCaseDto> dtos = input.stream().map(UpdateProfileSequenceInput::toDto).toList();

        List<ProfileDto> profileList = profileCommandService.updateProfileSequence(dtos, user.getId());

        return profileList.stream().map(ProfileResult::of).toList();
    }

    @Override
    @MutationMapping
    public ProfileResult updateProfileColor(@Argument @Valid UpdateProfileColorInput input,
            @ContextValue(value = "user", required = false) User userInput) {
        validateUser(userInput);

        return ProfileResult.of(profileCommandService.updateProfileColor(input.toDto()));
    }

    @Override
    @MutationMapping
    public List<ProfileResult> deleteProfile(@Argument Long deletedId,
            @ContextValue(value = "user", required = false) User userInput) {
        User user = validateUser(userInput);

        List<ProfileDto> profileList = profileCommandService.deleteProfile(deletedId, user.getId());

        return profileList.stream().map(ProfileResult::of).toList();
    }

    public User validateUser(User userInput) {
        return UserUtil.require(userInput);
    }
}
