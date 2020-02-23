package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.model.Image;
import com.dap.DailyArtPrompt.model.ImageResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ImageService {

    private final RestTemplate restTemplate;

    public ImageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Image getImage() {
        String uri = "https://dog.ceo/api/breeds/image/random";
        ImageResponse imageResponse = restTemplate.getForObject(uri, ImageResponse.class);
        if(imageResponse != null) {
            return new Image(imageResponse.getMessage());
        }
        else {
           return null;
        }
    }
}