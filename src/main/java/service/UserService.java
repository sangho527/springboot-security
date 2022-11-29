package service;

import domain.User;
import domain.dto.UserDto;
import domain.dto.UserJoinRequest;
import exception.ErrorCode;
import exception.HospitalReviewException;
import org.springframework.stereotype.Service;
import repository.UserRepository;

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
