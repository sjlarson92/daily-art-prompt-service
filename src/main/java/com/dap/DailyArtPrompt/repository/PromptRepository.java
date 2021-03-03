package com.dap.DailyArtPrompt.repository;

import com.dap.DailyArtPrompt.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PromptRepository extends JpaRepository<Prompt, Long> {
    Prompt findFirstByOrderByDateDesc();

    Optional<Prompt> findPromptByDate(LocalDate date);

}
