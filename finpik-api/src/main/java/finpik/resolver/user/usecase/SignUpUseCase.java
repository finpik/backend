package finpik.resolver.user.usecase;

import finpik.entity.User;
import finpik.repository.auth.AuthCacheRepository;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finpik.JwtProvider;
import finpik.dto.CreateTokenDto;
import finpik.resolver.user.usecase.dto.SignUpResultDto;
import finpik.resolver.user.usecase.dto.SignUpUseCaseDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpUseCase {
    private final UserRepository userRepository;
    private final AuthCacheRepository authCacheRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignUpResultDto signUp(SignUpUseCaseDto dto) {
        String email = authCacheRepository.getEmailByCustomId(dto.vendorId(), dto.provider());
        validateExistingUserBy(email);

        User user = dto.toDomain(email);

        User savedUser = userRepository.save(user);

        CreateTokenDto tokenDto = CreateTokenDto.builder()
            .userId(savedUser.getId())
            .email(savedUser.getEmail())
            .issuedAt(dto.issuedAt())
            .expiration(dto.expiresAt())
            .build();

        return new SignUpResultDto(
            savedUser,
            jwtProvider.createAccessToken(tokenDto),
            jwtProvider.createRefreshToken(tokenDto)
        );
    }

    private void validateExistingUserBy(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BusinessException(ErrorCode.EXISTING_USER);
        }
    }
}
