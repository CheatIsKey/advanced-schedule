package jpa.basic.advancedschedule.comment.dto;

import jpa.basic.advancedschedule.comment.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UpdateCommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static UpdateCommentResponse from(Comment comment) {
        return UpdateCommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}
