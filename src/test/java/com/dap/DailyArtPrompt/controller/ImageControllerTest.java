package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.service.ImageContentService;
import com.dap.DailyArtPrompt.service.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(ImageController.class)
@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ImageService imageService;

    @MockBean
    ImageContentService imageContentService;

    @BeforeEach
    public void resetMocks() {
        reset(imageContentService, imageService);
    }

    @Nested
    @DisplayName("/images")
    class getImage {

            @Test
            public void shouldReturnImage() throws Exception {
                UUID imageId = UUID.randomUUID();
                UUID promptId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();
                Image image = new Image(imageId, promptId, userId, "some name", "src", false, null);
                when(imageService.getImage(imageId)).thenReturn(image);
                mockMvc.perform(
                        get("/images/" + imageId)
                ).andExpect(content().string(objectMapper.writeValueAsString(image)));
            }
    }

    @Nested
    class updateImage {
        @Test
        public void returnUpdatedImage() throws Exception {
            UUID imageId = UUID.randomUUID();
            UUID promptId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();
            Image image = new Image(imageId, promptId, userId, "some name", "src", false, null);
            when(imageService.updateImage(image)).thenReturn(image);
            mockMvc.perform(put("/images/" + imageId)
                    .content(objectMapper.writeValueAsString(image))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(content().string(objectMapper.writeValueAsString(image)));
        }
    }

    @Nested
    class getImageFromS3Bucket {
        @Test
        public void getsImageContentByGivenId() throws Exception {
            UUID imageId = UUID.randomUUID();
            mockMvc.perform(get("/images/" + imageId + "/content"));
            verify(imageContentService).getImageContent(imageId);
        }

        @Test
        public void returnsRedirectView() throws Exception {
            UUID imageId = UUID.randomUUID();
            String url = "/images/" + imageId + "/content";
            String redirectUrl = "some fake url";
            RedirectView redirectView = new RedirectView(redirectUrl);
            when(imageContentService.getImageContent(imageId)).thenReturn(redirectView);
            mockMvc.perform(get(url))
                    .andExpect(redirectedUrl(redirectUrl));
        }
    }
}
