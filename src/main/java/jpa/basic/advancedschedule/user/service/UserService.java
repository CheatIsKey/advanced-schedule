package jpa.basic.advancedschedule.user.service;

import jpa.basic.advancedschedule.config.PasswordEncoder;
import jpa.basic.advancedschedule.exception.CustomException;
import jpa.basic.advancedschedule.exception.ErrorCode;
import jpa.basic.advancedschedule.schedule.entity.Schedule;
import jpa.basic.advancedschedule.schedule.service.ScheduleService;
import jpa.basic.advancedschedule.user.dto.*;
import jpa.basic.advancedschedule.user.entity.User;
import jpa.basic.advancedschedule.user.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ScheduleService scheduleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, @Lazy ScheduleService scheduleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.scheduleService = scheduleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public CreateUserResponse save(CreateUserRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(encodedPassword)
                .build();

        User savedUser = userRepository.save(user);

        return CreateUserResponse.from(savedUser);
    }

    public LoginUserResponse login(LoginUserRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        return LoginUserResponse.from(user);
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

        List<Schedule> schedules = scheduleService.getUserSchedules(userId);

        return ReadUserResponse.from(user, schedules);
    }

    @Transactional
    public UpdateUserResponse update(Long userId, UpdateUserRequest request, Long loginUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getId().equals(loginUserId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        /**
         * 세션이 같더라도, 비밀번호를 한 번 더 검증해서 보안을 올린다.
         */
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        user.change(request.name(), request.email(), encodedPassword);

        return UpdateUserResponse.from(user);
    }

    @Transactional
    public DeleteUserReponse delete(Long userId, DeleteUserRequest request, Long loginUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getId().equals(loginUserId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        /**
         * 사용자가 탈퇴하면, 해당 일정은 볼 수 없게 처리해야 한다.
         * @SoftDelete 기능으로 Update 쿼리로 자동 변환되서 수행하기에 해당 사용자와 연관된 일정들을 삭제 쿼리 날려주면 된다.
         */
        List<Schedule> schedules = scheduleService.getUserSchedules(userId);
        scheduleService.deleteAllSchedules(schedules);

        DeleteUserReponse dto = DeleteUserReponse.from(user, "삭제가 완료되었습니다.");

        userRepository.delete(user);

        return dto;
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
