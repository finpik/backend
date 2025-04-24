package com.loanpick.user.service;

import org.springframework.stereotype.Service;

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

        return userRepository.save(dto.toEntity(email));
    }
}
