package com.dap.DailyArtPrompt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class UserResponse {
    private long id;
    private String email;

}
