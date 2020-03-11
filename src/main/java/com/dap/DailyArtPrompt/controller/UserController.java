package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.service.UserService;
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
    public void createUser(
            @RequestHeader("email") String email,
            @RequestHeader("password") String password) {
        userService.createUser(email, password);
    }
}
