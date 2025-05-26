package finpik.resolver.user.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finpik.jwt.JwtProvider;
import finpik.jwt.dto.CreateTokenDto;
import finpik.resolver.user.usecase.dto.SignUpResultDto;
import finpik.resolver.user.usecase.dto.SignUpUseCaseDto;
import finpik.user.entity.User;
import finpik.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpUseCase {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignUpResultDto signUp(SignUpUseCaseDto dto) {
        User user = userService.createUser(dto.toCreateUserDto());

        CreateTokenDto tokenDto = CreateTokenDto.builder().userId(user.getId()).email(user.getEmail())
                .issuedAt(dto.issuedAt()).expiration(dto.expiresAt()).build();

        return new SignUpResultDto(user, jwtProvider.createAccessToken(tokenDto));
    }
}
