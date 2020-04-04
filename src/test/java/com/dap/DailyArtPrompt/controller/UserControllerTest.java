package com.dap.DailyArtPrompt.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.dap.DailyArtPrompt.model.UserResponse;
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
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @BeforeEach
    public void clearMocks() {
        reset(userService);
    }

    @Nested
    @DisplayName("/users")
    class createUser {

        @Test
        public void callsCreateUserWithCorrectParams() throws Exception {
            UserResponse userResponse = UserResponse.builder()
                    .id(1)
                    .email("fakeEmail@testing.com")
                    .build();

            ResponseEntity<UserResponse> userResponseEntity= ResponseEntity
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
                    .id(1)
                    .email("fakeEmail@testing.com")
                    .build();

            ResponseEntity<UserResponse> userResponseEntity= ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userResponse);

            when(userService
                    .createUser("fakeEmail@testing.com","NotMyPassword"))
                    .thenReturn(userResponseEntity);
            mockMvc
                    .perform(post("/users")
                        .header("email", "fakeEmail@testing.com")
                        .header("password", "NotMyPassword"))
                    .andExpect(content().string(objectMapper.writeValueAsString(userResponse)));
        }
    }
}
