package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.repository.ImageRepository;
import com.dap.DailyArtPrompt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private  final ImageRepository imageRepository;

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(
        @RequestHeader("email") String email,
        @RequestHeader("password") String password
    ) {
            log.info("Creating user with email: {}", email);
            return userService.createUser(email, password);
    }

    @GetMapping("/users/{id}/images")
    public List<Image> getUserImages(@PathVariable long id) {
        log.info("Getting all images for user id: " + id);
        return imageRepository.findAllByUserId(id);
    }
}
