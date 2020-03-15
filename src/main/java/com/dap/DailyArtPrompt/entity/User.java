package com.dap.DailyArtPrompt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "`user`", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    public User(String email, String password) {
        this.email = email;
        this. password = password;
    }
}
