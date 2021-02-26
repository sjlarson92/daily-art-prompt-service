package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.repository.ImageRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    ImageRepository imageRepository;

    @InjectMocks
    ImageService imageService;

    @Nested
    class getImage {
        @Test
        public void callsRepoWithCorrectParamsAndReturnImage() {
            UUID imageId = UUID.randomUUID();
            Image image = Image.builder()
                    .id(imageId)
                    .build();
            when(imageRepository.findById(imageId)).thenReturn(Optional.ofNullable(image));
            Image foundImage = imageService.getImage(imageId);
            assertThat(foundImage).isEqualTo(image);
        }

        @Nested
        class whenImageIdDoesNotExists {
            @Test
            public void shouldReturn404WithCorrectErrorMessage() {
                UUID imageId = UUID.randomUUID();
                when(imageRepository.findById(imageId)).thenReturn(Optional.empty());
                assertThatThrownBy(() -> imageService.getImage(imageId))
                        .isInstanceOf(ResponseStatusException.class)
                        .hasMessageContaining(HttpStatus.NOT_FOUND.toString())
                        .hasMessageContaining("No event found by given id: " + imageId);
            }
        }
    }

    @Nested
    class updateImage {
        @Test
        public void callsRepoWithCorrectParamAndReturnsImageWithUpdatedAt() {
            UUID imageId = UUID.randomUUID();
            Image image = Image.builder()
                    .id(imageId)
                    .build();
            when(imageRepository.save(image)).thenReturn(image);
            Image updatedImage = imageService.updateImage(image);
            assertThat(updatedImage.getUpdatedAt()).isNotNull();
        }
    }
}
