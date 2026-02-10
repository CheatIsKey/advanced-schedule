package jpa.basic.advancedschedule.schedule.dto;

import jpa.basic.advancedschedule.schedule.entity.Schedule;
import lombok.Builder;

@Builder
public record DeleteScheduleResponse(
        Long id,
        String title,
        String message
) {
    public static DeleteScheduleResponse from(Schedule schedule, String message) {
        return DeleteScheduleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .message(message)
                .build();
    }
}
