package jpa.basic.advancedschedule.user.dto;

import jpa.basic.advancedschedule.user.entity.User;
import lombok.Builder;

@Builder
public record LoginUserResponse(
        Long id,
        String email,
        String message
) {

    public static LoginUserResponse from(User user) {
        return LoginUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .message("환영합니다!")
                .build();
    }
}
