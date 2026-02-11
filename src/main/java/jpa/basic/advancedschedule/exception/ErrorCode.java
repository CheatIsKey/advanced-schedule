package jpa.basic.advancedschedule.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

//    공통 관련 에러 코드("A###")
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "A001", "비밀번호가 일치하지 않습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "A002", "입력값이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A003", "로그인이 필요한 기능입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A004", "권한이 없습니다."),

//    일정 관련 에러 코드("S###")
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "해당 일정은 존재하지 않습니다."),

//    유저 관련 에러 코드("U###")
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "해당 유저는 존재하지 않습니다"),

//    댓글 관련 에러 코드("C###")
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "해당 댓글은 존재하지 않습니다."),
    COMMENT_NOT_IN_SCHEDULE(HttpStatus.NOT_FOUND, "C002", "해당 일정에 속한 댓글이 아닙니다."),

//    로그인 관련 에러 코드("L###")
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "L001", "이메일 또는 비밀번호가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
