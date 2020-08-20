package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.entity.User;
import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.repository.ImageRepository;
import com.dap.DailyArtPrompt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Value("${dapBaseUrl}")
    private final String dapBaseUrl;


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

    public void createImageMetadata(long userId, String description) {
        UUID imageId = UUID.randomUUID();
        String url = dapBaseUrl + "/api/images/" + imageId + "/content";
        Image newImage = Image.builder()
                .id(imageId)
                .userId(userId)
                .description(description)
                .url(url)
                .build();
        log.info("Attempting to save image metadata: " + newImage);
        imageRepository.save(newImage);
    }
}
