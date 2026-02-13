package jpa.basic.advancedschedule.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ApiResponse<T>(
        boolean success,
        String code,
        T data,
        LocalDateTime timestamp
) {

    /**
     * 성공 트랜잭션 케이스에서 반환 데이터가 있는 경우,
     * 일관된 형식으로 보내주도록 제네릭으로 정의한다.
     */
    public static <T> ApiResponse<T> success(HttpStatus status, T data) {
        return new ApiResponse<>(true, String.valueOf(status.value()), data, LocalDateTime.now());
    }

    /**
     * 성공 트랜잭션 케이스에서 반환 데이터가 없는 경우,
     * HTTP 상태 코드와 시간만 담아서 보낸다.
     */
    public static <T> ApiResponse<T> success(HttpStatus status) {
        return new ApiResponse<>(true, String.valueOf(status.value()), null, LocalDateTime.now());
    }

    /**
     * 실패 트랜잭션 케이스(정리된 예외 열거 타입들)로 예외가 터졌을 때, 정보를 보내줘야 한다.
     *
     * @param errorCode : 정리된 예외들
     * @return : 실패 정보와 함께 정보가 담긴 ErrorResponse Dto를 반환
     */
    public static ApiResponse<ErrorResponse> fail(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getCode(),
                ErrorResponse.from(errorCode.getMessage()), LocalDateTime.now());
    }

    /**
     * 실패 트랜잭션 케이스(Bean Validation)로 예외가 터졌을 때, 어떤 필드가 문제인지 전부 출력한다.
     *
     * @param errorCode:   공통화된 에러 코드들
     * @param fieldErrors: 어떤 필드에서, 무슨 이유로 터졌는 지 알려준다.
     * @return: 실패 정보와 함께 정보가 담긴 ErrorResponse Dto를 반환
     */
    public static ApiResponse<ErrorResponse> fail(ErrorCode errorCode, List<FieldError> fieldErrors) {
        return new ApiResponse<>(false, errorCode.getCode(),
                ErrorResponse.of(errorCode.getMessage(), fieldErrors), LocalDateTime.now());
    }
}
