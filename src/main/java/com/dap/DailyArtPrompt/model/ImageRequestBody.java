package com.dap.DailyArtPrompt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageRequestBody {
    private String description;
    private byte[] image;
}
