package jpa.basic.advancedschedule.schedule.dto;

import jpa.basic.advancedschedule.schedule.entity.Schedule;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReadSchedulesResponse(
        Long id,
        String author,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static ReadSchedulesResponse from(Schedule schedule) {
        return ReadSchedulesResponse.builder()
                .id(schedule.getId())
                .author(schedule.getAuthor())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .build();
    }
}
