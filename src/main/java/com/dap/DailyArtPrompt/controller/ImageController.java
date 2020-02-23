package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.model.Image;
import com.dap.DailyArtPrompt.service.ImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("")
    public ResponseEntity<Image> getImage() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        Image image = imageService.getImage();
        return ResponseEntity.ok().headers(headers).body(image);
    }
}