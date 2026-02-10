package jpa.basic.advancedschedule.exception;

import lombok.Builder;

@Builder
public record ExceptionResponseDto(
        int status,
        String errorCode,
        String message
) {
}
