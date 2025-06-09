package finpik.resolver.user.resolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.LocalDateTime;

import finpik.entity.enums.RegistrationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import finpik.config.graphql.GraphqlScalarConfig;
import finpik.entity.enums.Gender;
import finpik.global.error.dispatcher.GraphQLExceptionHandlerDispatcher;
import finpik.global.error.handler.impl.DefaultExceptionHandler;
import finpik.global.error.resolver.GlobalExceptionResolver;
import finpik.resolver.user.usecase.SignUpUseCase;
import finpik.resolver.user.usecase.dto.SignUpResultDto;
import finpik.user.entity.User;

@GraphQlTest(UserResolver.class)
@Import({GlobalExceptionResolver.class, GraphQLExceptionHandlerDispatcher.class, DefaultExceptionHandler.class,
        GraphqlScalarConfig.class})
class UserResolverTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private SignUpUseCase signUpUseCase;

    @DisplayName("사용자가 회원가입 요청시 user정보와 accessToken이 리턴된다.")
    @Test
    void signUp() {
        // given
        User user = User.withId(
            1L, "test", "test@test.com", Gender.MALE,
            RegistrationType.KAKAO, LocalDateTime.now(), LocalDate.of(2025, 5, 25)
        );

        SignUpResultDto dto = new SignUpResultDto(user, "testAccessToken");
        given(signUpUseCase.signUp(any())).willReturn(dto);

        // when
        // then
        graphQlTester.document("""
                    mutation {
                        signUp(input: {
                            username: "테스트"
                            dateOfBirth: "2025-05-25"
                            gender: MALE
                            provider: "123123123"
                            vendorId: "kakao"
                        }) {
                            userId
                            accessToken
                        }
                    }
                """).execute().path("signUp.userId").entity(Long.class).isEqualTo(1L).path("signUp.accessToken")
                .entity(String.class).isEqualTo("testAccessToken");
    }
}
