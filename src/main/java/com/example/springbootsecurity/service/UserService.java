package com.example.springbootsecurity.service;

import com.example.springbootsecurity.repository.UserRepository;
import com.example.springbootsecurity.domain.User;
import com.example.springbootsecurity.domain.dto.UserDto;
import com.example.springbootsecurity.domain.dto.UserJoinRequest;
import com.example.springbootsecurity.exception.ErrorCode;
import com.example.springbootsecurity.exception.HospitalReviewException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto join(UserJoinRequest request) {
        //userName 중복체크
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> {
                    throw new HospitalReviewException(
                            ErrorCode.DUPLICATED_USER_NAME,
                            String.format("Username : %s", request.getUserName())
                    );
                });

        User savedUser = userRepository.save(request.toEntity());

        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmail())
                .build();
    }
}
