package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.model.ImageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ImageServiceTest {

    ImageService imageService = new ImageService();

    @Nested
    class getImage {
        @Nested
        @DisplayName("when image is null")
        class whenImageIsNull {
            @Test
            public void shouldReturnNull() {
                final String uri = "https://dog.ceo/api/breeds/image/random";
                RestTemplate restTemplate = new RestTemplate();
                when(restTemplate.getForObject(uri, ImageResponse.class)).thenReturn(null);
                assertThat(imageService.getImage()).isEqualTo(null);
            }
        }

    }
}