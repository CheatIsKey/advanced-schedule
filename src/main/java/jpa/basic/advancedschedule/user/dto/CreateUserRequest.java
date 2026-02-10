package jpa.basic.advancedschedule.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @NotBlank(message = "사용자명은 필수값입니다.")
        @Size(max = 20, message = "사용자명은 최대 20자까지 가능합니다.")
        String name,

        @NotBlank(message = "이메일은 필수값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수값입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상으로 적어야 합니다.")
        String password
) {
}
