package jpa.basic.advancedschedule.comment.controller;

import jakarta.validation.Valid;
import jpa.basic.advancedschedule.comment.dto.*;
import jpa.basic.advancedschedule.comment.service.CommentService;
import jpa.basic.advancedschedule.exception.ApiResponse;
import jpa.basic.advancedschedule.exception.CustomException;
import jpa.basic.advancedschedule.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules/{scheduleId}/comments/")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글을 작성할 때는 일정 PK와 사용자의 PK가 필요하다.
     * 세션에 등록된 사용자 PK로 판단한다.
     *
     * @param scheduleId: 댓글을 쓰려는 작성된 일정의 PK
     * @param userId:     댓글을 쓰려는 사용자의 PK
     * @param request:    댓글 내용을 가지고 있는 DTO
     * @return : 댓글이 작성된 일정의 제목과 댓글 INFO가 담긴 DTO
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CreateCommentResponse>> addComment(
            @PathVariable Long scheduleId,
            @SessionAttribute(name = "userId", required = false) Long userId,
            @Valid @RequestBody CreateCommentRequest request) {

        if (userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, commentService.save(scheduleId, userId, request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReadCommentsResponse>>> getComments(
            @PathVariable Long scheduleId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, commentService.getAll(scheduleId)));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponse<ReadCommentResponse>> getComment(
            @PathVariable Long commentId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, commentService.getOne(commentId)));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<UpdateCommentResponse>> updateComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request,
            @SessionAttribute(name = "userId", required = false) Long userId
    ) {

        if (userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, commentService.update(scheduleId, commentId, userId, request)));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<DeleteCommentResponse>> deleteComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @SessionAttribute(name = "userId", required = false) Long userId
    ) {

        if (userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, commentService.delete(scheduleId, commentId, userId)));
    }
}
