package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.User;
import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<UserResponse> createUser(String email, String password) throws DataIntegrityViolationException {
        User newUser = new User(email, password);
        try {
            User savedUser = userRepository.save(newUser);
            UserResponse userResponse = UserResponse.builder()
                    .id(savedUser.getId())
                    .email(savedUser.getEmail())
                    .build();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userResponse);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .header("message", "Email already in use. Please try again")
                    .build();
        }

    }
}