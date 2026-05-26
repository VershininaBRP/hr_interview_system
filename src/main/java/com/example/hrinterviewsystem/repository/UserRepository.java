package com.example.hrinterviewsystem.repository;

import com.example.hrinterviewsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);
}