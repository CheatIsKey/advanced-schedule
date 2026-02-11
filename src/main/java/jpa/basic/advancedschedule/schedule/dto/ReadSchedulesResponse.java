package jpa.basic.advancedschedule.schedule.dto;

import jpa.basic.advancedschedule.schedule.entity.Schedule;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReadSchedulesResponse(
        Long id,
        String title,
        String content,
        String author,
        Long commentsCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static ReadSchedulesResponse from(Schedule schedule, Long count) {
        return ReadSchedulesResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .author(schedule.getAuthor())
                .commentsCount(count)
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .build();
    }
}
