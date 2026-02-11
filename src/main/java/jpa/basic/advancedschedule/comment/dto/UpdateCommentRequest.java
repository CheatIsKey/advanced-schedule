package jpa.basic.advancedschedule.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCommentRequest(

        @NotBlank(message = "댓글 내용은 필수값입니다.")
        @Size(max = 50, message = "댓글 내용은 최대 50자까지 가능합니다.")
        String content
) {
}
