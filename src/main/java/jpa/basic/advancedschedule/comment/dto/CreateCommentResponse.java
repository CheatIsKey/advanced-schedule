package jpa.basic.advancedschedule.comment.dto;

import jpa.basic.advancedschedule.comment.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateCommentResponse(
        Long id,
        String scheduleName,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static CreateCommentResponse from(Comment comment) {
        return CreateCommentResponse.builder()
                .id(comment.getId())
                .scheduleName(comment.getSchedule().getTitle())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}
