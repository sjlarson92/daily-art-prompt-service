package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.User;
import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse createUser(String email, String password) {
        User newUser = new User(email, password);
        User savedUser = userRepository.save(newUser);
        return UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .build();
    }
}
