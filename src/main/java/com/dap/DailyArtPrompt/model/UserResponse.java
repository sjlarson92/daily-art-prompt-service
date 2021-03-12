package com.dap.DailyArtPrompt.model;

import com.dap.DailyArtPrompt.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String name;
    private User.Role role;

}
