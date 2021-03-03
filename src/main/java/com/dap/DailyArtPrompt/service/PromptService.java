package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Prompt;
import com.dap.DailyArtPrompt.entity.User;
import com.dap.DailyArtPrompt.repository.PromptRepository;
import com.dap.DailyArtPrompt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromptService {

    private final PromptRepository promptRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public Map<LocalDate, Prompt> getAllPrompts() {
        List<Prompt> allPromptsList = promptRepository.findAll();

        Map<LocalDate, Prompt> allPromptsMap = new HashMap<>();

        for (Prompt prompt : allPromptsList) {
            allPromptsMap.put(prompt.getDate(), prompt);
        }

        return allPromptsMap;
    }

    public Prompt getPromptByDate(LocalDate date) {

        Optional<Prompt> promptByDate = promptRepository.findPromptByDate(date);
        return promptByDate
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Prompt with date: " + date + " was not found"
                ));
    }

    public void createPrompts(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id: " + userId + " could not be found in db.");
        } else if (optionalUser.get().getRole() == User.Role.GODLIKE) {
            int wordAmount = 100; // TODO: move this var to be passed in request
            log.info("Now adding " + wordAmount + " prompts to the db.");
            List<String> response = restTemplate.exchange(
                    "https://random-word-api.herokuapp.com/word?swear=0&number=" + wordAmount,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<String>>() {
                    }
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
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authorized to add prompts.");
        }
    }
}
