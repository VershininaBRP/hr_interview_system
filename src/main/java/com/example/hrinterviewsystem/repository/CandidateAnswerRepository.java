package com.example.hrinterviewsystem.repository;

import com.example.hrinterviewsystem.entity.CandidateAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateAnswerRepository extends JpaRepository<CandidateAnswer, Long> {
}