package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.repository.PromptRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
//                assertThat(promptService.getAllPrompts()).isEqualTo();
            }
        }
    }

    @Nested
    class testFunc {
        @Test
        public void returnsHi() {
            Prompt prompt = new Prompt("2020-02-22", "Hi");
            when(promptRepository.getOne((long) 1)).thenReturn(prompt);
            String result = promptService.testFunc();
            assertThat(result).isEqualTo(prompt.getText());
        }
    }

}