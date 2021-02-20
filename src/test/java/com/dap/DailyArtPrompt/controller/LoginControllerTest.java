package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.service.LoginService;
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

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(LoginController.class)
@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    LoginService loginService;

    @BeforeEach
    public void clearMocks() {
        reset(loginService);
    }

    @Nested
    @DisplayName("/login")
    class validateLogin {

        @Test
        public void callsValidateUserWithCorrectParams() throws Exception {
            UserResponse userResponse = UserResponse.builder()
                    .id(UUID.randomUUID())
                    .email("email")
                    .build();
            ResponseEntity<UserResponse> userResponseEntity = ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(userResponse);
            when(loginService
                    .validateLogin("email","password"))
                    .thenReturn(userResponseEntity);

            mockMvc.perform(
                    post("/login")
                        .with(httpBasic("email", "password"))
            );
            verify(loginService).validateLogin("email", "password");
        }

        @Test
        public void returnsResponseEntityOfUserResponse() throws Exception{
            UserResponse userResponse = UserResponse.builder()
                    .id(UUID.randomUUID())
                    .email("email")
                    .build();
            ResponseEntity<UserResponse> userResponseEntity = ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(userResponse);
            when(loginService
                    .validateLogin("email","password"))
                    .thenReturn(userResponseEntity);
            mockMvc
                    .perform(post("/login")
                            .with(httpBasic("email", "password")))
                    .andExpect(content().string(objectMapper.writeValueAsString(userResponse)));
        }
    }

}
