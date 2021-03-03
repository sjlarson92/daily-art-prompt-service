package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.service.PromptService;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

    @GetMapping(path = "/prompts", params = "date")
    public Prompt getPromptByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Fetching prompt by date: " + date);
        return promptService.getPromptByDate(date);
    }

    @PostMapping("/prompts")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPrompts(@RequestParam UUID userId) {
        log.info("Generating prompts");
        promptService.createPrompts(userId);
    }
}
