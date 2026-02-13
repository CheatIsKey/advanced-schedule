package jpa.basic.advancedschedule.exception;

/**
 * 잘못 작성된 필드들에 대한 출력기
 *
 * @param field:         무슨 칸이 문제인지
 * @param rejectedValue: 잘못쓴 내용이 무엇인지
 * @param message:       왜 잘못된 건지
 */
public record FieldError(
        String field,
        String rejectedValue,
        String message
) {

    public static FieldError from(org.springframework.validation.FieldError error) {
        return new FieldError(
                error.getField(),
                error.getRejectedValue() != null ? error.getRejectedValue().toString() : "",
                error.getDefaultMessage()
        );
    }
}
