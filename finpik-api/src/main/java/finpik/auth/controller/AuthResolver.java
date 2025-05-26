package finpik.auth.controller;

import static finpik.util.Values.REFRESH_TOKEN;

import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import finpik.auth.controller.result.AuthResult;
import finpik.auth.usecase.TokenUseCase;
import finpik.auth.usecase.dto.TokenRefreshResultDto;
import graphql.GraphQLContext;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthResolver implements AuthApi {
    private final TokenUseCase tokenUseCase;

    @MutationMapping
    public AuthResult refresh(@ContextValue(REFRESH_TOKEN) String refreshToken, GraphQLContext context) {
        TokenRefreshResultDto resultDto = tokenUseCase.refresh(refreshToken);

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            attributes.setAttribute(REFRESH_TOKEN, resultDto.newRefreshToken(), RequestAttributes.SCOPE_REQUEST);
        }

        return new AuthResult(resultDto.newAccessToken());
    }
}
