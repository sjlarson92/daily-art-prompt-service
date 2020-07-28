package com.dap.DailyArtPrompt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    private UUID id;

    private long userId;

    private String imageName;

    private String src;

    // TODO: add default value of false
    private boolean liked;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "image")
    private List<Comment> comments;
}
