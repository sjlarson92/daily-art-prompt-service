package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.model.Image;
import com.dap.DailyArtPrompt.service.ImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{id}")
    public Image getImage(@PathVariable String id) {
        Image image = imageService.getImage();
        System.out.println("Api /images/{id} returns Image with following src: "
                + image.getSrc());
        return image;
    }
}

