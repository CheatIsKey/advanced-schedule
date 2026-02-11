package jpa.basic.advancedschedule.comment.service;

import jpa.basic.advancedschedule.comment.dto.*;
import jpa.basic.advancedschedule.comment.entity.Comment;
import jpa.basic.advancedschedule.comment.repository.CommentRepository;
import jpa.basic.advancedschedule.exception.CustomException;
import jpa.basic.advancedschedule.exception.ErrorCode;
import jpa.basic.advancedschedule.schedule.entity.Schedule;
import jpa.basic.advancedschedule.schedule.repository.ScheduleRepository;
import jpa.basic.advancedschedule.user.entity.User;
import jpa.basic.advancedschedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateCommentResponse save(Long scheduleId, Long userId, CreateCommentRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.content())
                .user(user)
                .schedule(schedule).build();

        Comment savedComment = commentRepository.save(comment);

        return CreateCommentResponse.from(savedComment);
    }

    public List<ReadCommentsResponse> getAll(Long scheduleId) {
        if (!scheduleRepository.existsById(scheduleId)) {
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
        if (!scheduleRepository.existsById(scheduleId)) {
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
        if (!scheduleRepository.existsById(scheduleId)) {
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
}
