package finpik.user.service;

import org.springframework.stereotype.Service;

import finpik.auth.repository.AuthCacheRepository;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.user.entity.User;
import finpik.user.repository.UserRepository;
import finpik.user.service.dto.CreateUserDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthCacheRepository authCacheRepository;

    public User createUser(CreateUserDto dto) {
        String email = authCacheRepository.getEmailByCustomId(dto.id(), dto.provider());
        validateExistingUserBy(email);

        return userRepository.save(dto.toUser(email));
    }

    public User findUserBy(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
    }

    private void validateExistingUserBy(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BusinessException(ErrorCode.EXISTING_USER);
        }
    }
}
