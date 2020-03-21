package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Slf4j
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
            log.info("Creating user with email: {}", email);
            return userService.createUser(email, password);
    }
}
