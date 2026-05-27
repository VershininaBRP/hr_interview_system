package com.example.hrinterviewsystem.controller;

import com.example.hrinterviewsystem.entity.Vacancy;
import com.example.hrinterviewsystem.repository.UserRepository;
import com.example.hrinterviewsystem.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.hrinterviewsystem.entity.Question;
import com.example.hrinterviewsystem.entity.Vacancy;
import com.example.hrinterviewsystem.repository.QuestionRepository;
import com.example.hrinterviewsystem.repository.ResultRepository;

@Controller
@RequestMapping("/hr")
public class HRController {

    @Autowired
    private VacancyRepository vacancyRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/panel")
    public String hrPanel(Model model) {

        model.addAttribute(
                "vacancies",
                vacancyRepository.findAll()
        );

        return "hr-panel";
    }

    @GetMapping("/vacancy/create")
    public String createVacancyPage(Model model) {

        model.addAttribute(
                "vacancy",
                new Vacancy()
        );

        return "create-vacancy";
    }

    @GetMapping("/results")
    public String resultsPage(Model model) {

        model.addAttribute(
                "results",
                resultRepository.findAllByOrderByScoreDesc()
        );

        return "results";
    }
    @GetMapping("/candidate/{id}")
    public String candidateInfo(
            @PathVariable Long id,
            Model model
    ) {

        model.addAttribute(
                "candidate",
                userRepository.findById(id).orElseThrow()
        );

        return "candidate-info";
    }

    @PostMapping("/vacancy/create")
    public String createVacancy(
            @ModelAttribute Vacancy vacancy
    ) {

        vacancyRepository.save(vacancy);

        return "redirect:/hr/panel";
    }

    @GetMapping("/vacancy/{id}/questions")
    public String vacancyQuestions(
            @PathVariable Long id,
            Model model
    ) {

        Vacancy vacancy = vacancyRepository
                .findById(id)
                .orElseThrow();

        model.addAttribute("vacancy", vacancy);

        model.addAttribute(
                "questions",
                questionRepository.findByVacancyId(id)
        );

        model.addAttribute(
                "question",
                new Question()
        );

        return "vacancy-questions";
    }

    @PostMapping("/vacancy/{id}/questions")
    public String addQuestion(
            @PathVariable Long id,
            @ModelAttribute Question question
    ) {

        Vacancy vacancy = vacancyRepository
                .findById(id)
                .orElseThrow();

        question.setId(null);
        question.setVacancy(vacancy);

        questionRepository.save(question);

        return "redirect:/hr/vacancy/" + id + "/questions";
    }
}