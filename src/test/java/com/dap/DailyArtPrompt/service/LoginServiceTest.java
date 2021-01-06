package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.User;
import com.dap.DailyArtPrompt.model.UserResponse;
import com.dap.DailyArtPrompt.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    LoginService loginService;

    @Nested
    class validateLogin {
        @Nested
        class whenUserResponseIsNotFoundInDB {
            @Test
            public void returnsResponseEntityWithCorrectStatus() {
                String email = "electronicmail@email.com";
                String password = "notMyPassword";
                when(userRepository.findByEmailAndPassword(email,password))
                        .thenReturn(null);
                assertThat(loginService.validateLogin(email, password)
                        .getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }
            @Test
            public void returnsResponseEntityWithCorrectHeader() {
                String email = "electronicmail@email.com";
                String password = "notMyPassword";
                when(userRepository.findByEmailAndPassword(email,password))
                        .thenReturn(null);
                assertThat(loginService.validateLogin(email, password).getHeaders().get("message").get(0))
                        .isEqualTo("An error occurred. Try again.");
            }
        }

        @Nested
        class whenUserResponseIsFoundInDB {
            @Test
            public void returnsResponseEntityWithCorrectStatus() {
                String email = "electronicmail@email.com";
                String password = "notMyPassword";
                UserResponse userResponse = new UserResponse(1, email, User.Role.FEEDER);
                when(userRepository.findByEmailAndPassword(email,password))
                        .thenReturn(userResponse);
                assertThat(loginService.validateLogin(email, password).getStatusCode())
                        .isEqualTo(HttpStatus.ACCEPTED);
            }
            @Test
            public void returnsResponseEntityWithCorrectHeader() {
                String email = "electronicmail@email.com";
                String password = "notMyPassword";
                UserResponse userResponse = new UserResponse(1, email, User.Role.FEEDER);
                when(userRepository.findByEmailAndPassword(email,password))
                        .thenReturn(userResponse);
                assertThat(loginService.validateLogin(email, password).getBody())
                        .isEqualTo(userResponse);
            }
        }
    }
}
