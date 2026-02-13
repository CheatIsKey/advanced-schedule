package jpa.basic.advancedschedule.comment.service;

import jpa.basic.advancedschedule.comment.dto.*;
import jpa.basic.advancedschedule.comment.entity.Comment;
import jpa.basic.advancedschedule.comment.repository.CommentRepository;
import jpa.basic.advancedschedule.exception.CustomException;
import jpa.basic.advancedschedule.exception.ErrorCode;
import jpa.basic.advancedschedule.schedule.entity.Schedule;
import jpa.basic.advancedschedule.schedule.service.ScheduleService;
import jpa.basic.advancedschedule.user.entity.User;
import jpa.basic.advancedschedule.user.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleService scheduleService;
    private final UserService userService;

    /**
     * 서비스 계층끼리 서로 의존하면서, 무한참조가 발생한다.
     * 이때 프록시 전략으로, 그나마 더 중요한 빈을 먼저 만들도록, @Lazy로 대리 객체를 주입한다.
     * 댓글은 일정이 있어야 존재하므로, 더 중요한 일정 빈을 먼저 작업한다.
     * 해당 메서드가 호출될 때, 실제 객체를 가져온다.
     */
    public CommentService(CommentRepository commentRepository, @Lazy ScheduleService scheduleService, UserService userService) {
        this.commentRepository = commentRepository;
        this.scheduleService = scheduleService;
        this.userService = userService;
    }

    @Transactional
    public CreateCommentResponse save(Long scheduleId, Long userId, CreateCommentRequest request) {
        Schedule schedule = scheduleService.getSchedule(scheduleId);

        User user = userService.getUser(userId);

        Comment comment = Comment.builder()
                .content(request.content())
                .user(user)
                .schedule(schedule).build();

        Comment savedComment = commentRepository.save(comment);

        return CreateCommentResponse.from(savedComment);
    }

    public List<ReadCommentsResponse> getAll(Long scheduleId) {
        if (!scheduleService.existsSchedule(scheduleId)) {
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        List<Comment> comments = commentRepository.findAllByScheduleId(scheduleId);

        return comments.stream()
                .map(ReadCommentsResponse::from)
                .toList();
    }

    public ReadCommentResponse getOne(Long commentId) {
        Comment comment = commentRepository.findByIdWithScheduleAndUser(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        return ReadCommentResponse.from(comment);
    }

    @Transactional
    public UpdateCommentResponse update(Long scheduleId, Long commentId, Long userId, UpdateCommentRequest request) {
        if (!scheduleService.existsSchedule(scheduleId)) {
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        Comment comment = commentRepository.findByIdWithScheduleAndUser(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        /**
         * 수정하려는 댓글이 요청받은 일정에 있는지 체크하기.
         */
        if (!comment.getSchedule().getId().equals(scheduleId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_IN_SCHEDULE);
        }

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        comment.change(request.content());

        return UpdateCommentResponse.from(comment);
    }

    @Transactional
    public DeleteCommentResponse delete(Long scheduleId, Long commentId, Long userId) {
        if (!scheduleService.existsSchedule(scheduleId)) {
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        Comment comment = commentRepository.findByIdWithScheduleAndUser(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getSchedule().getId().equals(scheduleId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_IN_SCHEDULE);
        }

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        DeleteCommentResponse dto = DeleteCommentResponse.from(comment);
        commentRepository.delete(comment);

        return dto;
    }

    public List<Comment> getSchedule(Long scheduleId) {
        return commentRepository.findAllByScheduleId(scheduleId);
    }
}
