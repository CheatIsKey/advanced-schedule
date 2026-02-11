package jpa.basic.advancedschedule.comment.dto;

import jpa.basic.advancedschedule.comment.entity.Comment;
import lombok.Builder;

@Builder
public record DeleteCommentResponse(
        Long id,
        String content,
        String message
) {

    public static DeleteCommentResponse from(Comment comment) {
        return DeleteCommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .message("삭제가 완료되었습니다.")
                .build();
    }
}
