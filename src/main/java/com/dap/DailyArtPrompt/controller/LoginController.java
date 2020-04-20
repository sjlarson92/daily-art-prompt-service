package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    LoginService loginService;

    public LoginController(LoginService loginService) {this.loginService = loginService;}

    @PostMapping
    public ResponseEntity<UserResponse> validateLogin(
            @RequestHeader(value = "Authorization") String authorization
    ) {
        log.info("Login Controller: " + authorization);
        // code below decodes the authorization obj to email and password:
        String base64Credentials = authorization.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        // credentials = username:password
        final String[] values = credentials.split(":", 2);
        System.out.println("Validating Login for email: " + Arrays.toString(values));
        return loginService.validateLogin(values[0], values[1]);
    }
}
