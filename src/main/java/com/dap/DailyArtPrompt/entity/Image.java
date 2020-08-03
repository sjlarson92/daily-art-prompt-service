package com.dap.DailyArtPrompt.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    private String description;

    private String url;

    // TODO: add default value of false
    private boolean liked;

    @OneToMany(mappedBy = "image")
    @JsonManagedReference
    private List<Comment> comments;
}
