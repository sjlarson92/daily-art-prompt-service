package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.repository.ImageRepository;
import com.dap.DailyArtPrompt.service.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ImageRepository imageRepository;

    @MockBean
    ImageService imageService;

    @Nested
    @DisplayName("/images")
    class getImage {

        @Nested
        class whenImageWithIdExists {

            @Test
            public void shouldReturnImage() throws Exception {
                UUID imageId = UUID.randomUUID();
                Image image = new Image(imageId, 1234, "some name", "src", false, null);
                when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
                mockMvc.perform(
                        get("/images/" + imageId)
                ).andExpect(content().string(objectMapper.writeValueAsString(image)));
            }
        }

        @Nested
        class whenImageWithIdDoesNotExists {

            @Test
            public void shouldReturn404WithCorrectErrorMessage() throws Exception {
                UUID imageId = UUID.randomUUID();
                when(imageRepository.findById(imageId)).thenReturn(Optional.empty());
                String message = Objects.requireNonNull(
                        mockMvc.perform(get("/images/" + imageId))
                                .andExpect(status().isNotFound())
                                .andReturn().getResolvedException()).getMessage();

                assertThat(message).contains("No event found by given id: " + imageId);
            }
        }
    }

    @Nested
    class getImageFromS3Bucket {
        @Test
        public void getsImageContentByGivenId() throws Exception {
            UUID imageId = UUID.randomUUID();
            mockMvc.perform(get("/images/" + imageId + "/content"));
            verify(imageService).getImageContent(imageId);
        }

        @Test
        public void returnsRedirectView() throws Exception {
            UUID imageId = UUID.randomUUID();
            RedirectView redirectView = new RedirectView();
            when(imageService.getImageContent(imageId)).thenReturn(redirectView);
            mockMvc.perform(get("/images/" + imageId + "/content"))
                    .andExpect(content().string(objectMapper.writeValueAsString(redirectView)));
            verify(imageService).getImageContent(imageId);
        }
    }
}
