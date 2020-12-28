package com.dap.DailyArtPrompt.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.service.PromptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PromptController.class)
@ExtendWith(MockitoExtension.class)
class PromptControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PromptService promptService;

    @Nested
    @DisplayName("/prompts")
    class getAllPrompts {

        @Nested
        class whenPromptsServiceReturnsMap {

            @Test
            public void shouldReturnMap() throws Exception {
                Map<LocalDate, Prompt> promptsMap = new HashMap<>();
                Prompt prompt = new Prompt(UUID.randomUUID(), LocalDate.now(), "I am a prompt");
                promptsMap.put(prompt.getDate(), prompt);
                when(promptService.getAllPrompts()).thenReturn(promptsMap);

                mockMvc
                    .perform(get("/prompts"))
                    .andExpect(content().string(objectMapper.writeValueAsString(promptsMap)));
            }
        }
    }

    @Nested
    class createPrompts {
        @Test
        public void shouldCallCreatePrompts() throws Exception {
            mockMvc.perform(post("/prompts"));
            verify(promptService).createPrompts();
        }
    }
}
