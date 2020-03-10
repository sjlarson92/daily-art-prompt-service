package com.dap.DailyArtPrompt.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping("")
    public void createUser() {
        System.out.println("Inside UserController");
    }
}
