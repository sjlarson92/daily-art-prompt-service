package com.dap.DailyArtPrompt.service;

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
                String email = "electronicmail@snob.com";
                String password = "notMyPassword";
                when(userRepository.findByEmailAndPassword(email,password))
                        .thenReturn(null);
                assertThat(loginService.validateLogin(email, password)
                        .getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }
            @Test
            public void returnsResponseEntityWithCorrectHeader() {

            }
        }

        @Nested
        class whenUserResponseIsFoundInDB {
            @Test
            public void returnsResponseEntityWithCorrectStatus() {

            }
            @Test
            public void returnsResponseEntityWithCorrectHeader() {

            }
        }
    }
}