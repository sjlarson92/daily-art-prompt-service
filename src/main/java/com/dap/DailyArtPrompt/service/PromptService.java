package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.repository.PromptRepository;
import java.time.LocalDate;
import java.util.*;

import lombok.RequiredArgsConstructor;
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

//    public Map<LocalDate, Prompt> getAllPrompts() {
//        List<Prompt> allPromptsList = promptRepository.findAll();
//
//        Map<LocalDate, Prompt> allPromptsMap = new HashMap<>();
//
//        for (Prompt prompt : allPromptsList) {
//            LocalDate date = LocalDate.parse(prompt.getDate());
//            allPromptsMap.put(date, prompt);
//        }
//
//        return allPromptsMap;
//    }

    public void createPrompts() {
        String response = restTemplate.exchange(
                "https://writingexercises.co.uk/php/nouns.php",
                HttpMethod.GET,
                null,
                String.class).getBody();
        System.out.println("response: " + response);
        List<String> words = Arrays.asList(response.split(" "));
        // TODO: get most recent date from db and then add to that
        LocalDate date = LocalDate.now();
        words.forEach(word -> {
            System.out.println("word: " + word);
            System.out.println("date: " + date);
            Prompt newPrompt = new Prompt();
            newPrompt.setId(UUID.randomUUID());
            newPrompt.setDate(date);
            newPrompt.setText(word);
            promptRepository.save(newPrompt);
            date.plusDays(1L);
        });
    }
}
