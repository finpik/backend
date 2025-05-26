package finpik.resolver.user.resolver;

import static finpik.util.Values.FIVE_MINUTE_MILL;

import java.util.Date;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import finpik.resolver.user.resolver.input.SignUpInput;
import finpik.resolver.user.resolver.result.SignUpResult;
import finpik.resolver.user.usecase.SignUpUseCase;
import finpik.resolver.user.usecase.dto.SignUpResultDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserResolver implements UserApi {
    private final SignUpUseCase signUpUseCase;

    @Override
    @MutationMapping
    public SignUpResult signUp(@Argument @Valid SignUpInput input) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + FIVE_MINUTE_MILL);

        SignUpResultDto dto = signUpUseCase.signUp(input.toDto(issuedAt, expiresAt));

        return SignUpResult.of(dto);
    }
}
