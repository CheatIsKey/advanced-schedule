package jpa.basic.advancedschedule.user.dto;

import jpa.basic.advancedschedule.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReadUsersResponse(
        Long id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static ReadUsersResponse from(User user) {
        return ReadUsersResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();
    }
}
