package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.entity.User;
import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.repository.ImageRepository;
import com.dap.DailyArtPrompt.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    UserRepository userRepository = mock(UserRepository.class);
    ImageRepository imageRepository = mock(ImageRepository.class);
    String dapBaseUrl = "any random url...";

    UserService userService = new UserService(userRepository, imageRepository, dapBaseUrl);

    @Nested
    class createUser {
        String email = "Daisy Daisy give me your answer true";
        String password ="I'm half crazy all for the love of you!";
        String name = "It won't be a stylish marriage, I can't afford a carriage!";
        @Nested
        class whenEmailIsInDatabase {
            @Test
            public void returnsResponseEntityWithCorrectStatus() {
                User testUser = new User(email, name, password);
                when(userRepository.save(any(User.class))).thenReturn(testUser);
                assertThat(userService.createUser(email, name, password).getStatusCode())
                        .isEqualTo(HttpStatus.CREATED);
            }

            @Test
            public void returnsResponseEntityWithCorrectBody() {
                User testUser = new User(email, name, password);
                UserResponse userResponse = UserResponse.builder()
                        .id(testUser.getId())
                        .email(testUser.getEmail())
                        .name(testUser.getName())
                        .role(testUser.getRole())
                        .build();
                when(userRepository.save(any(User.class))).thenReturn(testUser);
                assertThat(userService.createUser(email,name, password).getBody())
                        .isEqualTo(userResponse);
            }
        }

        @Nested
        class whenEmailIsNotInDatabase {
            @Test
            public void returnsResponseEntityWithCorrectStatus() {
                when(userRepository.save(any(User.class)))
                        .thenThrow(DataIntegrityViolationException.class);
                assertThat(userService.createUser(email, name, password).getStatusCode())
                        .isEqualTo(HttpStatus.CONFLICT);
            }

            @Test
            public void returnsResponseEntityWithCorrectHeader() {
                String email = "fakeEmail";
                String password ="notAGoodPassword";
                when(userRepository.save(any(User.class)))
                        .thenThrow(DataIntegrityViolationException.class);
                //noinspection ConstantConditions
                assertThat(userService.createUser(email, name, password).getHeaders().get("message").get(0))
                        .isEqualTo("Email already in use. Please try again");
            }
        }
    }

    @Nested
    class createImageMetadata {
        @Test
        public void savesImageWithCorrectMetadata() {
            UUID userId = UUID.randomUUID();
            UUID promptId = UUID.randomUUID();
            String description = "Halloween is coming soon!";
            userService.createImageMetadata(userId, promptId, description);
            ArgumentCaptor<Image> argumentCaptor = ArgumentCaptor.forClass(Image.class);
            verify(imageRepository).save(argumentCaptor.capture());
            Image image = argumentCaptor.getValue();
            assertThat(image.getUserId()).isEqualTo(userId);
            assertThat(image.getPromptId()).isEqualTo(promptId);
            assertThat(image.getDescription()).isEqualTo(description);
            assertThat(image.getUrl()).startsWith(dapBaseUrl + "/api/images");
            assertThat(image.getUrl()).endsWith("/content");
        }

        @Test
        public void returnsSavedImage() {
            String description = "your mom is a desc";
            UUID userId = UUID.randomUUID();
            UUID promptId = UUID.randomUUID();
            Image image = Image.builder()
                    .description(description)
                    .userId(userId)
                    .build();
            when(imageRepository.save(any(Image.class))).thenReturn(image);
            Image result = userService.createImageMetadata(userId, promptId, description);
            assertThat(result).isEqualTo(image);
        }
    }
}
