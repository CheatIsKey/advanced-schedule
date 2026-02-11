package jpa.basic.advancedschedule.comment.dto;

import jpa.basic.advancedschedule.comment.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReadCommentResponse(
        Long id,
        String scheduleName,
        String userName,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static ReadCommentResponse from(Comment comment) {
        return ReadCommentResponse.builder()
                .id(comment.getId())
                .scheduleName(comment.getSchedule().getTitle())
                .userName(comment.getUser().getName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}
