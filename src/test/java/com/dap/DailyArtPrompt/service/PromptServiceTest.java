package com.dap.DailyArtPrompt.service;

import static java.lang.Integer.parseInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.repository.PromptRepository;
import java.time.LocalDate;
import java.util.*;

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

@ExtendWith(MockitoExtension.class)
class PromptServiceTest {
    @Mock
    PromptRepository promptRepository;

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
        @Test
        public void shouldCallCorrectApiWithCorrectParams() {
            String wordAmount = "2";
            ResponseEntity<List<String>> responseEntity = new ResponseEntity<>(List.of("winter", "wonderland"), HttpStatus.ACCEPTED);
            when(restTemplate.exchange(
                    "https://random-word-api.herokuapp.com/word?swear=0&number=" + wordAmount,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<String>>() {}
                    )).thenReturn(responseEntity);
            promptService.createPrompts();
            verify(restTemplate).exchange(
                    "https://random-word-api.herokuapp.com/word?swear=0&number=" + wordAmount,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<String>>() {});
        }

        @Test
        public void shouldSaveNewPrompts() {
            String wordAmount = "2";
            ResponseEntity<List<String>> responseEntity = new ResponseEntity<>(List.of("winter", "wonderland"), HttpStatus.ACCEPTED);
            when(restTemplate.exchange(
                    "https://random-word-api.herokuapp.com/word?swear=0&number=" + wordAmount,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<String>>() {}
            )).thenReturn(responseEntity);
            promptService.createPrompts();
            verify(promptRepository, times(parseInt(wordAmount))).save(any(Prompt.class));
        }

    }
}
