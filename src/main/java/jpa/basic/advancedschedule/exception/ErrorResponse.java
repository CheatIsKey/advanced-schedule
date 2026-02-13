package jpa.basic.advancedschedule.exception;

import lombok.Builder;

import java.util.List;

@Builder
public record ErrorResponse(
        String message,
        List<FieldError> fieldErrors
) {
    /**
     * 정의된 예외(ErrorCode)의 메시지만 가지고 출력하는 경우의 코드
     *
     * @param message: 정의된 예외(ErrorCode)의 메시지
     * @return: 메시지만 담긴 Dto 객체 반환
     */
    public static ErrorResponse from(String message) {
        return ErrorResponse.builder()
                .message(message)
                .build();
    }

    public static ErrorResponse of(String message, List<FieldError> fieldErrors) {
        return new ErrorResponse(message, fieldErrors);
    }
}
