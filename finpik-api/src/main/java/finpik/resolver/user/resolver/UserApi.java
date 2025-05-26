package finpik.resolver.user.resolver;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import finpik.resolver.user.resolver.input.SignUpInput;
import finpik.resolver.user.resolver.result.SignUpResult;
import jakarta.validation.Valid;

@Controller
public interface UserApi {
    SignUpResult signUp(@Argument @Valid SignUpInput input);
}
