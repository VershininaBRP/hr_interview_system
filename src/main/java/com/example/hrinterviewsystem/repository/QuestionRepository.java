package com.example.hrinterviewsystem.repository;

import com.example.hrinterviewsystem.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository
        extends JpaRepository<Question, Long> {

    List<Question> findByVacancyId(Long vacancyId);
}