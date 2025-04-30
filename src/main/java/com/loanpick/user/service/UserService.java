package com.loanpick.user.service;

import org.springframework.stereotype.Service;

import com.loanpick.error.enums.ErrorCode;
import com.loanpick.error.exception.BusinessException;
import com.loanpick.redis.service.CustomRedisService;
import com.loanpick.user.entity.User;
import com.loanpick.user.repository.UserRepository;
import com.loanpick.user.service.dto.CreateUserDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CustomRedisService customRedisService;

    @Transactional
    public User createUser(CreateUserDto dto) {
        String email = customRedisService.getEmailByCustomId(dto.id(), dto.provider());
        validateExistingUserBy(email);

        return userRepository.save(dto.toEntity(email));
    }

    private void validateExistingUserBy(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BusinessException(ErrorCode.EXISTING_USER);
        }
    }
}
