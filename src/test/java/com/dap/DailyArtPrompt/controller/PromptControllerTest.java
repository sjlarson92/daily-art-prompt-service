package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.service.PromptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
    @DisplayName("/prompt/all")
    class getAllPrompts {

        @Nested
        class whenPromptsServiceReturnsMap {

            @Test
            public void shouldReturnMap() throws Exception {
                Map<LocalDate, Prompt> promptsMap = new HashMap<>();
                Prompt prompt = new Prompt("2020-02-22", "I am a prompt");
                LocalDate date = LocalDate.parse("2020-02-22");
                promptsMap.put(date, prompt);
                when(promptService.getAllPrompts()).thenReturn(promptsMap);

                mockMvc.perform(get("/prompt/all"))
                        .andExpect(content().string(objectMapper.writeValueAsString(promptsMap)));

            }
        }
    }

}