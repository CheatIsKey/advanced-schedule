package jpa.basic.advancedschedule.schedule.dto;

import jpa.basic.advancedschedule.schedule.entity.Schedule;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReadScheduleResponse (
        Long id,
        String author,
        String title,
        String content,
        String userName,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt

) {
    public static ReadScheduleResponse from(Schedule schedule) {
        return ReadScheduleResponse.builder()
                .id(schedule.getId())
                .author(schedule.getAuthor())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .userName(schedule.getUser().getName())
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .build();
    }
}
