package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.entity.User;
import com.dap.DailyArtPrompt.repository.PromptRepository;
import com.dap.DailyArtPrompt.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromptServiceTest {
    @Mock
    PromptRepository promptRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    PromptService promptService;

    @Nested
    class getAllPrompts {

        @Nested
        class whenRepoReturnsListOfPrompts {

            @Test
            public void shouldReturnMap() {
                Prompt prompt = new Prompt(UUID.randomUUID(), LocalDate.now(), "I need to do laundry");
                List<Prompt> promptList = List.of(prompt);
                when(promptRepository.findAll()).thenReturn(promptList);
                Map<LocalDate, Prompt> expectedResult = new HashMap<>();
                expectedResult.put(prompt.getDate(), prompt);
                assertThat(promptService.getAllPrompts()).isEqualTo(expectedResult);
            }
        }

        @Nested
        class whenRepoReturnsEmptyListOfPrompts {

            @Test
            public void shouldReturnEmptyMap() {
                List<Prompt> promptList = new ArrayList<>();
                when(promptRepository.findAll()).thenReturn(promptList);
                Map<LocalDate, Prompt> expectedResult = new HashMap<>();
                assertThat(promptService.getAllPrompts()).isEqualTo(expectedResult);
            }
        }

        @Nested
        class whenRepoReturnsNull {

            @Test
            public void shouldThrowAnException() {
                when(promptRepository.findAll()).thenReturn(null);
                try {
                    assertThat(promptService.getAllPrompts())
                        .isInstanceOf(NullPointerException.class);
                } catch (Exception ignored) {}
            }
        }
    }

    @Nested
    class createPrompts {

        @Nested
        class whenUserIsGODLIKE {
            @Test
            public void shouldCallCorrectApiWithCorrectParams() {
                UUID userId = UUID.randomUUID();
                int wordAmount = 100;
                User godlikeUser = new User();
                godlikeUser.setRole(User.Role.GODLIKE);
                ResponseEntity<List<String>> responseEntity = new ResponseEntity<>(List.of("winter", "wonderland"), HttpStatus.ACCEPTED);
                when(userRepository.findById(userId)).thenReturn(Optional.of(godlikeUser));
                when(restTemplate.exchange(
                        "https://random-word-api.herokuapp.com/word?swear=0&number=" + wordAmount,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<String>>() {}
                )).thenReturn(responseEntity);
                promptService.createPrompts(userId);
                verify(restTemplate).exchange(
                        "https://random-word-api.herokuapp.com/word?swear=0&number=" + wordAmount,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<String>>() {});
            }

            @Test
            public void shouldSaveNewPrompts() {
                UUID userId = UUID.randomUUID();
                int wordAmount = 100;
                User godlikeUser = new User();
                godlikeUser.setRole(User.Role.GODLIKE);
                List<String> words = List.of("winter", "wonderland");
                ResponseEntity<List<String>> responseEntity = new ResponseEntity<>(words, HttpStatus.ACCEPTED);
                when(userRepository.findById(userId)).thenReturn(Optional.of(godlikeUser));
                when(restTemplate.exchange(
                        "https://random-word-api.herokuapp.com/word?swear=0&number=" + wordAmount,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<String>>() {}
                )).thenReturn(responseEntity);
                promptService.createPrompts(userId);
                verify(promptRepository, times(words.size())).save(any(Prompt.class));
            }
        }

        @Nested
        class whenUserIsNotGODLIKE {

            @Test
            public void shouldThrowUnauthorizedException() {
                UUID userId = UUID.randomUUID();
                User godlikeUser = new User();
                godlikeUser.setRole(User.Role.FEEDER);
                when(userRepository.findById(userId)).thenReturn(Optional.of(godlikeUser));
                assertThatThrownBy(() -> promptService.createPrompts(userId))
                        .isInstanceOf(ResponseStatusException.class)
                        .hasMessageContaining(HttpStatus.UNAUTHORIZED.toString())
                        .hasMessageContaining("User is not authorized to add prompts.");

            }
        }

        @Nested
        class whenUserDoesNotExist {

            @Test
            public void shouldThrowNotFoundException() {
                UUID userId = UUID.randomUUID();
                when(userRepository.findById(userId)).thenReturn(Optional.empty());
                assertThatThrownBy(() -> promptService.createPrompts(userId))
                        .isInstanceOf(ResponseStatusException.class)
                        .hasMessageContaining(HttpStatus.NOT_FOUND.toString())
                        .hasMessageContaining("User with id: " + userId + " could not be found in db");
            }
        }


    }
}
