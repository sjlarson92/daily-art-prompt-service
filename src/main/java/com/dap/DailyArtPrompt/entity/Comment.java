package com.dap.DailyArtPrompt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {

    @Id
    private UUID id;

    private UUID imageId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String text;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;


}
