package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.repository.PromptRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromptServiceTest {

    @Mock
    PromptRepository promptRepository;

    @InjectMocks
    PromptService promptService;

    @Nested
    class getAllPrompts {
        @Nested
        class whenRepoReturnsListOfPrompts {
            @Test
            public void shouldReturnMap() {
                Prompt prompt = new Prompt("2020-02-23", "I need to do laundry");
                List<Prompt> promptList = List.of(prompt);
                when(promptRepository.findAll()).thenReturn(promptList);
                Map<LocalDate, Prompt> expectedResult = new HashMap<>();
                expectedResult.put(LocalDate.parse(prompt.getDate()), prompt);
                assertThat(promptService.getAllPrompts()).isEqualTo(expectedResult);
            }
        }

        @Nested
        class whenRepoReturnsEmptyListOfPrompts {
            @Test
            public void  shouldReturnEmptyMap() {
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
                    assertThat(promptService.getAllPrompts()).isInstanceOf(NullPointerException.class);
                } catch (Exception ignored) {}

            }
        }
    }


}