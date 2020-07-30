package com.dap.DailyArtPrompt.repository;

import com.dap.DailyArtPrompt.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    List<Image> findAllByUserId(long id);
}