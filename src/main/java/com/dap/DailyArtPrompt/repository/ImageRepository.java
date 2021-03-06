package com.dap.DailyArtPrompt.repository;

import com.dap.DailyArtPrompt.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
    List<Image> findAllByUserId(UUID id);

    List<Image> findAllByPromptIdAndUserId(UUID promptId, UUID userId);

    List<Image> findAllByPromptIdAndUserIdNot(UUID promptId, UUID userId);
}
