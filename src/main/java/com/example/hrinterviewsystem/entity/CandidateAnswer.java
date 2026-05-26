package com.example.hrinterviewsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidate_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;

    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}