package com.dap.DailyArtPrompt.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.dap.DailyArtPrompt.model.Image;
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
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(ImageController.class)
@ExtendWith(MockitoExtension.class)
class ImageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ImageService imageService;

    @Nested
    @DisplayName("/image")
    class getImage {

        @Nested
        @DisplayName("When ImageService returns an Image")
        class whenImageServiceReturnsAnImage {

            @Test
            public void returnsTheImage() throws Exception {
                Image image = new Image("image source");
                when(imageService.getImage()).thenReturn(image);
                MvcResult result = mockMvc.perform(get("/image")).andReturn();

                String responseBodyString = result.getResponse().getContentAsString();
                System.out.println("response: " + responseBodyString);
                Image responseBody = objectMapper.readValue(responseBodyString, Image.class);

                assertThat(responseBody).isEqualToComparingFieldByField(image);
            }
        }

        @Nested
        @DisplayName("When ImageService returns null")
        class whenImageServiceReturnsNull {

            @Test
            public void returnsEmptyString() throws Exception {
                when(imageService.getImage()).thenReturn(null);
                MvcResult result = mockMvc.perform(get("/image")).andReturn();

                String responseBodyString = result.getResponse().getContentAsString();
                assertThat(responseBodyString).isEqualTo("");
            }
        }
    }
}
