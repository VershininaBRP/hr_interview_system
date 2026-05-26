package com.example.hrinterviewsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Column(unique = true)
    private String login;

    private String password;

    private String role;

    @OneToMany(mappedBy = "user")
    private List<CandidateAnswer> answers;

    @OneToMany(mappedBy = "user")
    private List<Result> results;
}