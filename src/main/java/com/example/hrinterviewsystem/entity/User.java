package com.example.hrinterviewsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String password;

    private String role;

    private String fullName;

    private String email;

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String about;
}