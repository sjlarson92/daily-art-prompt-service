package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.service.PromptService;
import java.time.LocalDate;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/prompts")
public class PromptController {
    final PromptService promptService;

    public PromptController(PromptService promptService) {
        this.promptService = promptService;
    }

    @GetMapping("")
    public Map<LocalDate, Prompt> getAllPrompts() {
        Map<LocalDate, Prompt> promptsMap = promptService.getAllPrompts();

        log.info("Api /prompts returns map with following prompts: ");
        for (Prompt prompt : promptsMap.values()) {
            System.out.println(
                "id: " +
                prompt.getId() +
                " date: " +
                prompt.getDate() +
                " text: " +
                prompt.getText()
            );
        }

        return promptsMap;
    }
}
