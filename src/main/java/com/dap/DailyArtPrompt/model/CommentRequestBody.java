package com.dap.DailyArtPrompt.model;

import lombok.Data;

import java.util.UUID;

@Data
public class CommentRequestBody {
    private UUID imageId;
    private UUID userId;
    private String text;
}
