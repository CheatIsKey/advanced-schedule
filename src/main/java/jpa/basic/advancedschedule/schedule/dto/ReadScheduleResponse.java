package jpa.basic.advancedschedule.schedule.dto;

import jpa.basic.advancedschedule.comment.dto.ReadCommentsResponse;
import jpa.basic.advancedschedule.schedule.entity.Schedule;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * author  : 일정 작성자 닉네임
 * userName: 로그인할 때 사용한 작성자 이름
 */
@Builder
public record ReadScheduleResponse(
        Long id,
        String author,
        String title,
        String content,
        String userName,
        List<ReadCommentsResponse> comments,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt

) {
    public static ReadScheduleResponse from(Schedule schedule, List<ReadCommentsResponse> commentsDto) {
        return ReadScheduleResponse.builder()
                .id(schedule.getId())
                .author(schedule.getAuthor())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .userName(schedule.getUser().getName())
                .comments(commentsDto)
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .build();
    }
}
