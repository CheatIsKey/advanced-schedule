package jpa.basic.advancedschedule.user.dto;

import jpa.basic.advancedschedule.user.entity.User;
import lombok.Builder;

@Builder
public record DeleteUserReponse(
        Long id,
        String message
) {
    public static DeleteUserReponse from(User user, String message) {
        return DeleteUserReponse.builder()
                .id(user.getId())
                .message(message)
                .build();
    }
}
