package jpa.basic.advancedschedule.user.service;

import jpa.basic.advancedschedule.exception.CustomException;
import jpa.basic.advancedschedule.exception.ErrorCode;
import jpa.basic.advancedschedule.schedule.entity.Schedule;
import jpa.basic.advancedschedule.schedule.repository.ScheduleRepository;
import jpa.basic.advancedschedule.user.dto.*;
import jpa.basic.advancedschedule.user.entity.User;
import jpa.basic.advancedschedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CreateUserResponse save(CreateUserRequest request) {
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();

        User savedUser = userRepository.save(user);

        return CreateUserResponse.from(savedUser);
    }

    public List<ReadUsersResponse> getAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(ReadUsersResponse::from)
                .toList();
    }

    public ReadUserResponse getOne(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Schedule> schedules = scheduleRepository.findByUserId(userId);

        return ReadUserResponse.from(user, schedules);
    }

    @Transactional
    public UpdateUserResponse update(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.change(request.name(), request.email(), request.password());

        return UpdateUserResponse.from(user);
    }

    @Transactional
    public DeleteUserReponse delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        DeleteUserReponse dto = DeleteUserReponse.from(user, "삭제가 완료되었습니다.");

        userRepository.deleteById(userId);

        return dto;
    }
}
