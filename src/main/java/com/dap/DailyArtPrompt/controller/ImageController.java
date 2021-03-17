package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.service.ImageContentService;
import com.dap.DailyArtPrompt.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageContentService imageContentService;

    private final ImageService imageService;

    @GetMapping("/images/{id}")
    public Image getImage(@PathVariable UUID id) {
        log.info("Fetching image with id " + id);
        return imageService.getImage(id);
    }

    @GetMapping("/images")
    public List<Image> getImageByPromptAndUserId(@RequestParam UUID promptId, @RequestParam UUID userId) {
        log.info("Fetching image with promptId: " + promptId + " and userId: " + userId );
        return imageService.getImageByPromptAndUserId(promptId, userId);
    }

    @GetMapping("/community-images")
    public List<Image> getCommunityImagesByPromptIdAndUserId(@RequestParam UUID promptId, @RequestParam UUID userId) {
        log.info("Fetching community images with promptId: " + promptId);
        return imageService.getCommunityImagesByPromptIdAndUserId(promptId, userId);
    }

    @PutMapping("/images/{id}")
    public Image updateImage(@PathVariable UUID id, @RequestBody Image image) {
        log.info("Updating image for id: " + id);
        return imageService.updateImage(image);
    }

    @DeleteMapping("/images/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable UUID id) {
        log.info("Deleting image for id: " + id);
        imageService.deleteImage(id);
    }
    @GetMapping("/images/{id}/content")
    public RedirectView getImageFromS3Bucket(@PathVariable UUID id) {
        log.info("Fetching image with id: " + id);
        return imageContentService.getImageContent(id);
    }

}
