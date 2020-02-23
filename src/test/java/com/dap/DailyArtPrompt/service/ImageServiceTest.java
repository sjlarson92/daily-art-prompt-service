package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.model.Image;
import com.dap.DailyArtPrompt.model.ImageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    ImageService imageService;

    @Nested
    class getImage {
        @Nested
        @DisplayName("when image is null")
        class whenImageIsNull {

            @Test
            public void shouldReturnNull() {
                String uri = "https://dog.ceo/api/breeds/image/random";
                when(restTemplate.getForObject(uri, ImageResponse.class)).thenReturn(null);
                assertThat(imageService.getImage()).isEqualTo(null);
            }
        }

        @Nested
        @DisplayName("when image is returned")
        class whenImageResponseIsReturned {

            @Test
            public void shouldReturnNewImage() {
                String uri = "https://dog.ceo/api/breeds/image/random";
                ImageResponse imageResponse = new ImageResponse("Image Response message", "status");
                when(restTemplate.getForObject(uri, ImageResponse.class)).thenReturn(imageResponse);
                Image image = new Image(imageResponse.getMessage());
                Image result = imageService.getImage();
                assertThat(result).isEqualToComparingFieldByField(image);
            }
        }

    }
}