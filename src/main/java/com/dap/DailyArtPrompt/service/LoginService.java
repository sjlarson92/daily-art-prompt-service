package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    public ResponseEntity<UserResponse> validateLogin(String email, String password) {
        System.out.println("Validating Login for email in LoginService: " + email);

            UserResponse userResponse = userRepository.findByEmailAndPassword(email, password);
            System.out.println("userResponse: " + userResponse);
        if(userResponse != null) {
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(userResponse);
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("message", "An error occurred. Try again.")
                    .build();
        }


    }
}
