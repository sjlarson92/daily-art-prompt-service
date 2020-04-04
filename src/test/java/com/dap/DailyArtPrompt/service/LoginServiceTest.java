package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    @Mock
    UserRepository userRepository;

    @Nested
    class validateLogin {
        @Nested
        class whenUserResponseIsNotFoundInDB {
            @Test
            public void returnsResponseEntityWithCorrectStatus() {

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