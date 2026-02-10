package jpa.basic.advancedschedule.user.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jpa.basic.advancedschedule.exception.CustomException;
import jpa.basic.advancedschedule.exception.ErrorCode;
import jpa.basic.advancedschedule.user.dto.*;
import jpa.basic.advancedschedule.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<CreateUserResponse> addUser(
            @Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(
            @Valid @RequestBody LoginUserRequest request, HttpSession session) {
        LoginUserResponse dto = userService.login(request);

        /**
         * 세션에는 객체를 저장하면 메모리 차지가 심하고 DB와 데이터 불일치 문제가 있을 수 있다.
         * PK를 저장하도록 하자.
         */
        session.setAttribute("userId", dto.id());

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<ReadUsersResponse>> getUser() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ReadUserResponse> getUser(
            @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getOne(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UpdateUserResponse> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRequest request,
            @SessionAttribute(name = "userId", required = false) Long loginUserId) {

        if (loginUserId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return ResponseEntity.status(HttpStatus.OK).body(userService.update(userId, request, loginUserId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<DeleteUserReponse> deleteUser(
            @PathVariable Long userId,
            @RequestBody DeleteUserRequest request,
            @SessionAttribute(name = "userId",  required = false) Long loginUserId,
            HttpSession session) {

        if (loginUserId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        DeleteUserReponse dto = userService.delete(userId, request, loginUserId);

        session.invalidate();

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
