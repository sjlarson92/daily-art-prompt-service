package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.service.PromptService;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prompts")
public class PromptController {
    final PromptService promptService;

    public PromptController(PromptService promptService) {
        this.promptService = promptService;
    }

    @GetMapping("")
    public ResponseEntity<Map<LocalDate, Prompt>> getAllPrompts() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        Map<LocalDate, Prompt> promptsMap = promptService.getAllPrompts();

        System.out.println("Api /all returns map with following prompts: ");
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

        return ResponseEntity.ok().headers(headers).body(promptsMap);
    }
}
