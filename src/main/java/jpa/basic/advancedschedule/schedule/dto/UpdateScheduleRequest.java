package jpa.basic.advancedschedule.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateScheduleRequest(
        @NotBlank(message = "작성자명은 필수값입니다.")
        @Size(max = 20, message = "작성자명은 최대 20자까지 가능합니다.")
        String author,

        @NotBlank(message = "일정 제목은 필수값입니다.")
        @Size(max = 50, message = "일정 제목은 최대 50자까지 가능합니다.")
        String title,

        @NotBlank(message = "일정 내용은 필수값입니다.")
        @Size(max = 50, message = "일정 내용은 최대 50자까지 가능합니다.")
        String content
) {
}
