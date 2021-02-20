package com.dap.DailyArtPrompt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "`user`")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private UUID id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = Role.FEEDER;
        this.id = UUID.randomUUID();
    }

    public enum Role {
        GODLIKE,
        FEEDER
    }
}
