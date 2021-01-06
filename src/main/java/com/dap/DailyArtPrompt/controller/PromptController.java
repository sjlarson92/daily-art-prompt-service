package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.service.PromptService;
import java.time.LocalDate;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PromptController {

    final PromptService promptService;

    @GetMapping("/prompts")
    public Map<LocalDate, Prompt> getAllPrompts() {
        log.info("Fetching prompts");
        return promptService.getAllPrompts();
    }

    @PostMapping("/prompts")
    public void createPrompts() {
        log.info("Generating prompts");
        promptService.createPrompts();
    }
}
