package jpa.basic.advancedschedule.user.dto;

import jpa.basic.advancedschedule.schedule.entity.Schedule;
import jpa.basic.advancedschedule.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReadUserResponse(
        Long id,
        String name,
        String email,
        List<ReadUserSchedulesResponse> dtos,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static ReadUserResponse from(User user, List<Schedule> schedules) {
        return ReadUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .dtos(schedules.stream()
                        .map(ReadUserSchedulesResponse::from)
                        .toList())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();
    }
}
