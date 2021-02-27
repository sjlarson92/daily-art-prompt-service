package com.dap.DailyArtPrompt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    private UUID id;

    private UUID promptId;

    private UUID userId;

    private String description;

    private String url;

    private boolean liked;

    private OffsetDateTime updatedAt;
}
