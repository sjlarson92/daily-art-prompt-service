package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.model.ImageRequestBody;
import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.repository.ImageRepository;
import com.dap.DailyArtPrompt.service.ImageService;
import com.dap.DailyArtPrompt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private  final ImageRepository imageRepository;
    private final ImageService imageService;

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

    @PostMapping("/users/{id}/images")
    public ResponseEntity createUserImage(
            @PathVariable long id,
            @ModelAttribute ImageRequestBody imageRequestBody
    ) throws IOException {
        log.info("file: " + imageRequestBody.getFile());
        log.info("description " + imageRequestBody.getDescription());
        UUID imageId = userService.createImageMetadata(id, imageRequestBody.getDescription());
        imageService.saveImageToS3(imageId, imageRequestBody.getFile());
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
