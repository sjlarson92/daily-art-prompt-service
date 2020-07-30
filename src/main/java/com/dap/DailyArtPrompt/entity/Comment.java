package com.dap.DailyArtPrompt.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "image_id")
    @JsonBackReference
    private Image image;

    // TODO: default value is false
    private boolean deleted;

    // TODO: default value is false
    private boolean editing;

    private String text;


}
