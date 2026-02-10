package jpa.basic.advancedschedule.user.dto;

import jpa.basic.advancedschedule.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateUserResponse(
        Long id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static CreateUserResponse from(User user) {
        return CreateUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();
    }
}
