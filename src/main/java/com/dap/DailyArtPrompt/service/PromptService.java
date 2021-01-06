package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.repository.PromptRepository;
import java.time.LocalDate;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PromptService {

    private final PromptRepository promptRepository;
    private final RestTemplate restTemplate;

    public Map<LocalDate, Prompt> getAllPrompts() {
        List<Prompt> allPromptsList = promptRepository.findAll();

        Map<LocalDate, Prompt> allPromptsMap = new HashMap<>();

        for (Prompt prompt : allPromptsList) {
            allPromptsMap.put(prompt.getDate(), prompt);
        }

        return allPromptsMap;
    }

    public void createPrompts() {
        int wordAmount = 100;
        List<String> response = restTemplate.exchange(
                "https://random-word-api.herokuapp.com/word?swear=0&number=" + wordAmount,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
                ).getBody();
        Prompt mostRecentPrompt = promptRepository.findFirstByOrderByDateDesc();
        LocalDate mostRecentDate = mostRecentPrompt != null
                ? mostRecentPrompt.getDate()
                : LocalDate.now().minusDays(1);
        int counter = 1;
        if (response != null) {
            for (String word : response) {
                Prompt newPrompt = new Prompt();
                newPrompt.setId(UUID.randomUUID());
                newPrompt.setDate(mostRecentDate.plusDays(counter));
                newPrompt.setText(word);
                promptRepository.save(newPrompt);
                counter++;
            }
        }
    }
}
