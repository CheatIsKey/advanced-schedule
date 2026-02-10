package jpa.basic.advancedschedule.schedule.dto;

import jpa.basic.advancedschedule.schedule.entity.Schedule;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateScheduleResponse(
        Long id,
        String author,
        String title,
        String content,
        Long userId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {


    public static CreateScheduleResponse from(Schedule schedule) {
        return CreateScheduleResponse.builder()
                .id(schedule.getId())
                .author(schedule.getAuthor())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .createdAt(schedule.getCreatedAt())
                .userId(schedule.getUser().getId())
                .modifiedAt(schedule.getModifiedAt())
                .build();
    }
}
