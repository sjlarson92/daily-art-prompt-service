package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.repository.CommentRepository;
import com.dap.DailyArtPrompt.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final CommentRepository commentRepository;

    public Image getImage(UUID id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No event found by given id: " + id));
    }
    public Image updateImage(Image image) {
        image.setUpdatedAt(OffsetDateTime.now());
        return imageRepository.save(image);
    }

    public List<Image> getImageByPromptAndUserId(UUID promptId, UUID userId) {
        return imageRepository.findAllByPromptIdAndUserId(promptId, userId);
    }

    public List<Image> getCommunityImagesByPromptIdAndUserId(UUID promptId, UUID userId) {
        return imageRepository.findAllByPromptIdAndUserIdNot(promptId, userId);
    }

    public void deleteImage(UUID id) {
        commentRepository.deleteAllByImageId(id);
        imageRepository.deleteById(id);
    }
}
