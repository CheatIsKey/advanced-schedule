package jpa.basic.advancedschedule.schedule.controller;

import jakarta.validation.Valid;
import jpa.basic.advancedschedule.exception.ApiResponse;
import jpa.basic.advancedschedule.exception.CustomException;
import jpa.basic.advancedschedule.exception.ErrorCode;
import jpa.basic.advancedschedule.schedule.dto.*;
import jpa.basic.advancedschedule.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<CreateScheduleResponse>> addSchedule(
            @PathVariable Long userId, @Valid @RequestBody CreateScheduleRequest request,
            @SessionAttribute(name = "userId", required = false) Long loginUserId) {

        if (loginUserId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, scheduleService.save(userId, request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ReadSchedulesResponse>>> getSchedules(
            @PageableDefault(size = 10, page = 0, sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, scheduleService.getAll(pageable)));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<ReadScheduleResponse>> getSchedule(
            @PathVariable Long scheduleId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, scheduleService.getOne(scheduleId)));
    }

    @PatchMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<UpdateScheduleResponse>> updateSchedule(
            @PathVariable Long scheduleId, @Valid @RequestBody UpdateScheduleRequest request,
            @SessionAttribute(name = "userId", required = false) Long loginUserId) {

        if (loginUserId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, scheduleService.update(scheduleId, request, loginUserId)));
    }

    /**
     * SoftDelete로 실제로 삭제 X
     * 'HttpStatus.NO_CONTENT'로 지정하면 뒤에 body 반환이 있어도 Void로 처리.
     * 따라서 결과를 JSON으로 반환하려면 HttpStatus.OK로 설정한다.
     *
     * @param scheduleId: 일정 PK
     * @return : 간단한 필드만 JSON으로 담아서 반환
     */
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<DeleteScheduleResponse>> deleteSchedule(
            @PathVariable Long scheduleId,
            @Valid @RequestBody DeleteScheduleRequest request,
            @SessionAttribute(name = "userId", required = false) Long loginUserId) {

        if (loginUserId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        DeleteScheduleResponse dto = scheduleService.delete(scheduleId, request, loginUserId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, dto));
    }
}
