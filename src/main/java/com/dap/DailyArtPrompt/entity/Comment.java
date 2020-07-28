package com.dap.DailyArtPrompt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "imageId")
    private Image image;

    // TODO: default value is false
    private boolean deleted;

    // TODO: default value is false
    private boolean editing;

    private String text;
}
