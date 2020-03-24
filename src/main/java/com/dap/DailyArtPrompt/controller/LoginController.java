package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    LoginService loginService;

    public LoginController(LoginService loginService) {this.loginService = loginService;}

    @PostMapping
    public ResponseEntity<UserResponse> validateLogin(
            @RequestHeader("email") String email,
            @RequestHeader("password") String password
    ) {
        System.out.println("Validating Login for email: " + email);
        return loginService.validateLogin(email, password);
    }
}
