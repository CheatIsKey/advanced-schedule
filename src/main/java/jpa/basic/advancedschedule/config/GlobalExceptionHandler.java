package jpa.basic.advancedschedule.config;

import jpa.basic.advancedschedule.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Enum으로 지정한 예외들 중 선택해서 상황에 따라 다르게 출력
     *
     * @param exception : 예외가 발생해서 들어온 예외들
     * @return : 보기 쉽게 가공해서 전달
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.fail(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleException(MethodArgumentNotValidException exception) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

        List<FieldError> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::from)
                .toList();

        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.fail(errorCode, fieldErrors));
    }
}
