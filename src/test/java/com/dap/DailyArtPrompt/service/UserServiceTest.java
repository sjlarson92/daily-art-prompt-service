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
        @Nested
        class whenEmailIsInDatabase {
            @Test
            public void returnsResponseEntityWithCorrectStatus() {
                String email = "fakeEmail";
                String password ="notAGoodPassword";
                User testUser = new User(1,email, password);
                when(userRepository.save(any(User.class))).thenReturn(testUser);
                assertThat(userService.createUser(email, password).getStatusCode())
                        .isEqualTo(HttpStatus.CREATED);
            }

            @Test
            public void returnsResponseEntityWithCorrectBody() {
                String email = "fakeEmail";
                String password ="notAGoodPassword";
                User testUser = new User(1, email, password);
                UserResponse userResponse = UserResponse.builder()
                        .id(testUser.getId())
                        .email(testUser.getEmail())
                        .build();
                when(userRepository.save(any(User.class))).thenReturn(testUser);
                assertThat(userService.createUser(email,password).getBody())
                        .isEqualTo(userResponse);
            }
        }

        @Nested
        class whenEmailIsNotInDatabase {
            @Test
            public void returnsResponseEntityWithCorrectStatus() {
                String email = "fakeEmail";
                String password ="notAGoodPassword";
                when(userRepository.save(any(User.class)))
                        .thenThrow(DataIntegrityViolationException.class);
                assertThat(userService.createUser(email, password).getStatusCode())
                        .isEqualTo(HttpStatus.CONFLICT);
            }

            @Test
            public void returnsResponseEntityWithCorrectHeader() {
                String email = "fakeEmail";
                String password ="notAGoodPassword";
                when(userRepository.save(any(User.class)))
                        .thenThrow(DataIntegrityViolationException.class);
                //noinspection ConstantConditions
                assertThat(userService.createUser(email, password).getHeaders().get("message").get(0))
                        .isEqualTo("Email already in use. Please try again");
            }
        }
    }

    @Nested
    class createImageMetadata {
        @Test
        public void savesImageWithCorrectMetadata() {
            long userId = 2;
            String description = "Halloween is coming soon!";
            userService.createImageMetadata(userId, description);
            ArgumentCaptor<Image> argumentCaptor = ArgumentCaptor.forClass(Image.class);
            verify(imageRepository).save(argumentCaptor.capture());
            Image image = argumentCaptor.getValue();
            assertThat(image.getUserId()).isEqualTo(userId);
            assertThat(image.getDescription()).isEqualTo(description);
            assertThat(image.getUrl()).startsWith(dapBaseUrl + "/api/images");
            assertThat(image.getUrl()).endsWith("/content");
        }

        @Test
        public void returnsSavedImage() {
            String description = "your mom is a desc";
            long userId = 9999;
            Image image = Image.builder()
                    .description(description)
                    .userId(userId)
                    .build();
            when(imageRepository.save(any(Image.class))).thenReturn(image);
            Image result = userService.createImageMetadata(userId, description);
            assertThat(result).isEqualTo(image);
        }
    }
}
