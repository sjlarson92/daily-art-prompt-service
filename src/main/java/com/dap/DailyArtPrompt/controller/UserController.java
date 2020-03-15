package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<UserResponse> createUser(
        @RequestHeader("email") String email,
        @RequestHeader("password") String password
    ) {
        try {
            UserResponse userResponse = userService.createUser(email, password);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userResponse);
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .header("message", "Email already in use")
                    .build();
        }

    }
}
