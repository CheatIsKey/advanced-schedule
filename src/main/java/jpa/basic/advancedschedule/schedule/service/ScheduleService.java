package jpa.basic.advancedschedule.schedule.service;

import jakarta.validation.Valid;
import jpa.basic.advancedschedule.comment.dto.ReadCommentsResponse;
import jpa.basic.advancedschedule.comment.entity.Comment;
import jpa.basic.advancedschedule.comment.repository.CommentRepository;
import jpa.basic.advancedschedule.config.PasswordEncoder;
import jpa.basic.advancedschedule.exception.CustomException;
import jpa.basic.advancedschedule.exception.ErrorCode;
import jpa.basic.advancedschedule.schedule.dto.*;
import jpa.basic.advancedschedule.schedule.entity.Schedule;
import jpa.basic.advancedschedule.schedule.repository.ScheduleRepository;
import jpa.basic.advancedschedule.user.entity.User;
import jpa.basic.advancedschedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CreateScheduleResponse save(Long userId, CreateScheduleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Schedule schedule = Schedule.createSchedule(request, user);

        Schedule savedSchedule = scheduleRepository.save(schedule);

        return CreateScheduleResponse.from(savedSchedule);
    }

    public Page<ReadSchedulesResponse> getAll(Pageable pageable) {
//        List<Schedule> schedules = scheduleRepository.findAll();
        return scheduleRepository.findAllWithCommentsCount(pageable);
    }

    public ReadScheduleResponse getOne(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        List<Comment> comments = commentRepository.findAllByScheduleId(scheduleId);

        List<ReadCommentsResponse> dto = comments.stream()
                .map(ReadCommentsResponse::from)
                .toList();

        return ReadScheduleResponse.from(schedule, dto);
    }

    @Transactional
    public UpdateScheduleResponse update(Long scheduleId, @Valid UpdateScheduleRequest request, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (!schedule.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        schedule.change(request.author(), request.title(), request.content());

        return UpdateScheduleResponse.from(schedule);
    }

    @Transactional
    public DeleteScheduleResponse delete(Long scheduleId, @Valid DeleteScheduleRequest request, Long loginUserId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (!schedule.getUser().getId().equals(loginUserId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        DeleteScheduleResponse dto = DeleteScheduleResponse.from(schedule, "삭제가 완료되었습니다.");

        scheduleRepository.deleteById(scheduleId);

        return dto;
    }
}
