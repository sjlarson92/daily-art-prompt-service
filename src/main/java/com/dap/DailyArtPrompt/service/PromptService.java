package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.repository.PromptRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PromptService {

    private final PromptRepository promptRepository;

    public PromptService(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }

    public Map<LocalDate, Prompt> getAllPrompts() {
        List<Prompt> allPromptsList = promptRepository.findAll();

        Map<LocalDate, Prompt> allPromptsMap = new HashMap<>();

        for (Prompt prompt : allPromptsList) {
            LocalDate date = LocalDate.parse(prompt.getDate());
            allPromptsMap.put(date, prompt);
        }

        return allPromptsMap;
    }
}