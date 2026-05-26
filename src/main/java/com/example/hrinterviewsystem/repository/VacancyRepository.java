package com.example.hrinterviewsystem.repository;

import com.example.hrinterviewsystem.entity.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}