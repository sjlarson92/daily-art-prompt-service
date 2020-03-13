package com.dap.DailyArtPrompt.entity;

import lombok.Data;

import javax.persistence.*;
// TODO: add AllArgsConstructor
@Entity
@Table(name = "`user`", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this. password = password;
    }
}
