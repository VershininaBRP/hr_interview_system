package com.example.hrinterviewsystem.controller;

import com.example.hrinterviewsystem.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.hrinterviewsystem.entity.Question;
import com.example.hrinterviewsystem.entity.Result;
import com.example.hrinterviewsystem.entity.User;
import com.example.hrinterviewsystem.entity.Vacancy;
import com.example.hrinterviewsystem.repository.QuestionRepository;
import com.example.hrinterviewsystem.repository.ResultRepository;
import com.example.hrinterviewsystem.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private VacancyRepository vacancyRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/panel")
    public String candidatePanel(Model model) {

        model.addAttribute(
                "vacancies",
                vacancyRepository.findAll()
        );

        return "candidate-panel";
    }

    @GetMapping("/vacancy/{id}")
    public String interviewPage(
            @PathVariable Long id,
            Model model,
            Authentication authentication
    ) {

        Vacancy vacancy = vacancyRepository
                .findById(id)
                .orElseThrow();
        User user = userRepository.findByLogin(
                authentication.getName()
        );

        boolean alreadyPassed =
                resultRepository.existsByUserIdAndVacancyId(
                        user.getId(),
                        id
                );

        if (alreadyPassed) {

            model.addAttribute(
                    "message",
                    "Вы уже проходили это собеседование"
            );

            return "already-passed";
        }
        List<Question> questions =
                questionRepository.findByVacancyId(id);

        model.addAttribute("vacancy", vacancy);

        model.addAttribute("questions", questions);

        return "interview";
    }
    @PostMapping("/vacancy/{id}/submit")
    public String submitInterview(
            @PathVariable Long id,

            @RequestParam List<String> answers,

            Authentication authentication,

            Model model
    ) {

        Vacancy vacancy = vacancyRepository
                .findById(id)
                .orElseThrow();

        List<Question> questions =
                questionRepository.findByVacancyId(id);

        int totalScore = 0;

        int maxScore = 0;

        for (int i = 0; i < questions.size(); i++) {

            Question question = questions.get(i);

            maxScore += question.getWeight();

            String correct =
                    question.getCorrectAnswer()
                            .trim()
                            .toLowerCase();

            String userAnswer =
                    answers.get(i)
                            .trim()
                            .toLowerCase();

            if (correct.equals(userAnswer)) {

                totalScore += question.getWeight();
            }
        }

        double percent =
                ((double) totalScore / maxScore) * 100;

        String status;

        if (percent >= 80) {

            status = "Подходит";

        } else if (percent >= 50) {

            status = "Возможно подходит";

        } else {

            status = "Не подходит";
        }

        User user = userRepository.findByLogin(
                authentication.getName()
        );

        Result result = new Result();

        result.setUser(user);

        result.setVacancy(vacancy);

        result.setScore(percent);

        result.setStatus(status);

        resultRepository.save(result);

        model.addAttribute("score", percent);

        model.addAttribute("status", status);

        return "result";
    }
}