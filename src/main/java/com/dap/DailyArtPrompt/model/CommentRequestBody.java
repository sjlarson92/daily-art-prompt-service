package com.dap.DailyArtPrompt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestBody {

    @NotNull
    private UUID imageId;

    @NotNull
    private UUID userId;

    @NotEmpty
    private String text;
}
