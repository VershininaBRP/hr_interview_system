package com.example.hrinterviewsystem.repository;

import com.example.hrinterviewsystem.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository
        extends JpaRepository<Result, Long> {

    boolean existsByUserIdAndVacancyId(
            Long userId,
            Long vacancyId
    );

    List<Result> findAllByOrderByScoreDesc();
}