package com.dap.DailyArtPrompt.entity;

import javax.persistence.*;

@Entity
@Table
public class Prompt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String date;

    private String text;

    public Prompt() {}

    public Prompt(String date, String text) {
        this.date = date;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}
