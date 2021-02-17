package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.repository.ImageRepository;
import com.dap.DailyArtPrompt.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageRepository imageRepository;

    private final ImageService imageService;

    @GetMapping("/images/{id}")
    public Image getImage(@PathVariable UUID id) {
        log.info("Fetching image with id " + id);
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No event found by given id: " + id));
    }

    @GetMapping("/images/{id}/content")
    public RedirectView getImageFromS3Bucket(@PathVariable UUID id) {
        log.info("Fetching image with id: " + id);
        return imageService.getImageContent(id);
    }

}
