package jpa.basic.advancedschedule.schedule.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jpa.basic.advancedschedule.exception.CustomException;
import jpa.basic.advancedschedule.exception.ErrorCode;
import jpa.basic.advancedschedule.schedule.dto.*;
import jpa.basic.advancedschedule.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/{userId}")
    public ResponseEntity<CreateScheduleResponse> addSchedule(
            @PathVariable Long userId, @Valid @RequestBody CreateScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<ReadSchedulesResponse>> getSchedules() {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getAll());
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ReadScheduleResponse> getSchedule(
            @PathVariable Long scheduleId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getOne(scheduleId));
    }

    @PatchMapping("/{scheduleId}")
    public ResponseEntity<UpdateScheduleResponse> updateSchedule(
            @PathVariable Long scheduleId, @Valid @RequestBody UpdateScheduleRequest request,
            @SessionAttribute(name = "userId", required = false) Long userId) {

        if (userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.update(scheduleId, request, userId));
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
    public ResponseEntity<DeleteScheduleResponse> deleteSchedule(
            @PathVariable Long scheduleId,
            @Valid @RequestBody DeleteScheduleRequest request,
            @SessionAttribute(name = "userId", required = false) Long loginUserId) {

        if (loginUserId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        DeleteScheduleResponse dto = scheduleService.delete(scheduleId, request, loginUserId);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
