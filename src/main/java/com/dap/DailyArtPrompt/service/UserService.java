package com.dap.DailyArtPrompt.service;

import org.springframework.stereotype.Component;

@Component
public class UserService {
    public void createUser(String email, String password) {
        System.out.println("email is: " + email);
        System.out.println("password is: " + password);

        // check db if email exists there
    }
}
