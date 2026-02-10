package jpa.basic.advancedschedule.config;

import jpa.basic.advancedschedule.exception.CustomException;
import jpa.basic.advancedschedule.exception.ErrorCode;
import jpa.basic.advancedschedule.exception.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Enum으로 지정한 예외들 중 선택해서 상황에 따라 다르게 출력
     *
     * @param exception : 예외가 발생해서 들어온 예외들
     * @return : 보기 쉽게 가공해서 전달
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponseDto> handleException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus())
                .body(ExceptionResponseDto.builder()
                        .status(errorCode.getStatus().value())
                        .errorCode(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleException(MethodArgumentNotValidException exception) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

        /**
         * Bean Validation 유효성 검사 오류 중 첫 번째 에러 메시지 할당
         */
        String message = exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity.status(errorCode.getStatus())
                .body(ExceptionResponseDto.builder()
                        .status(errorCode.getStatus().value())
                        .errorCode(errorCode.getCode())
                        .message(message)
                        .build());
    }
}
