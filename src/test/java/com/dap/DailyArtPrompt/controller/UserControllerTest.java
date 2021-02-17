package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.model.ImageRequestBody;
import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.repository.ImageRepository;
import com.dap.DailyArtPrompt.service.ImageService;
import com.dap.DailyArtPrompt.service.UserService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    ImageRepository imageRepository;

    @MockBean
    ImageService imageService;

    @BeforeEach
    public void clearMocks() {
        reset(userService, imageService);
    }

    @Nested
    @DisplayName("/users")
    class createUser {

        @Test
        public void callsCreateUserWithCorrectParams() throws Exception {
            UserResponse userResponse = UserResponse.builder()
                    .id(UUID.randomUUID())
                    .email("fakeEmail@testing.com")
                    .build();

            ResponseEntity<UserResponse> userResponseEntity = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userResponse);
            when(userService
                    .createUser("fakeEmail@testing.com", "NotMyPassword"))
                    .thenReturn(userResponseEntity);
            mockMvc.perform(
                    post("/users")
                            .header("email", "fakeEmail@testing.com")
                            .header("password", "NotMyPassword")
            );
            verify(userService).createUser("fakeEmail@testing.com", "NotMyPassword");

        }

        @Test
        public void returnsUserResponse() throws Exception {
            UserResponse userResponse = UserResponse.builder()
                    .id(UUID.randomUUID())
                    .email("fakeEmail@testing.com")
                    .build();

            ResponseEntity<UserResponse> userResponseEntity = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userResponse);

            when(userService
                    .createUser("fakeEmail@testing.com", "NotMyPassword"))
                    .thenReturn(userResponseEntity);
            mockMvc
                    .perform(post("/users")
                            .header("email", "fakeEmail@testing.com")
                            .header("password", "NotMyPassword"))
                    .andExpect(content().string(objectMapper.writeValueAsString(userResponse)));
        }
    }

    @Nested
    class getUserImages {

        @Nested
        class whenRepoHasImagesForGivenUser {
            @Test
            public void returnsAListOfImages() throws Exception {
                UUID userId = UUID.randomUUID();
                Image image1 = new Image(UUID.randomUUID(), UUID.randomUUID(), userId, "some desc", "url", false);
                Image image2 = new Image(UUID.randomUUID(), UUID.randomUUID(), userId, "I am an image", "some url", true);

                List<Image> images = List.of(image1, image2);
                when(imageRepository.findAllByUserId(userId)).thenReturn(images);

                mockMvc.perform(
                        get("/users/" + userId + "/images")
                ).andExpect(content().string(objectMapper.writeValueAsString(images)));
            }
        }
    }

    @Nested
    class createUserImage {
        UUID userId = UUID.randomUUID();
        UUID imageId = UUID.randomUUID();
        UUID promptId = UUID.randomUUID();
        String userImageUrl = "/users/" + userId + "/images?promptId=" + promptId;
        MultipartFile file = new MockMultipartFile(
                "file",
                "some string".getBytes());
        ImageRequestBody imageRequestBody = new ImageRequestBody(
                "some description",
                file
        );
        Image image = Image.builder()
                .userId(userId)
                .id(imageId)
                .description(imageRequestBody.getDescription())
                .build();

        @Test
        public void passesCorrectParamsToUserService() throws Exception {
            when(userService.createImageMetadata(userId, promptId, imageRequestBody.getDescription()))
                    .thenReturn(image);
            mockMvc.perform(
                    post(userImageUrl)
                            .flashAttr("imageRequestBody", imageRequestBody));
            verify(userService).createImageMetadata(userId, promptId, imageRequestBody.getDescription());
        }

        @Test
        public void savesImageToS3() throws Exception {
            when(userService.createImageMetadata(userId, promptId, imageRequestBody.getDescription()))
                    .thenReturn(image);
            mockMvc.perform(
                    post(userImageUrl)
                            .flashAttr("imageRequestBody", imageRequestBody));
            verify(imageService).saveImageToS3(imageId, imageRequestBody.getFile());
        }

        @Test
        public void returnsImage() throws Exception {
            when(userService.createImageMetadata(userId, promptId, imageRequestBody.getDescription()))
                    .thenReturn(image);
            mockMvc.perform(
                    post(userImageUrl)
                            .flashAttr("imageRequestBody", imageRequestBody))
                    .andExpect(content()
                            .string(objectMapper.writeValueAsString(image)));
        }

        @Test
        public void returnsCorrectStatus() throws Exception {
            when(userService.createImageMetadata(userId, promptId, imageRequestBody.getDescription()))
                    .thenReturn(image);
            mockMvc.perform(
                    post(userImageUrl)
                        .flashAttr("imageRequestBody", imageRequestBody)
            ).andExpect(status().is(201));
        }

    }
}
