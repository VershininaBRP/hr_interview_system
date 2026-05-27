package com.example.hrinterviewsystem.controller;

import com.example.hrinterviewsystem.entity.Question;
import com.example.hrinterviewsystem.entity.User;
import com.example.hrinterviewsystem.entity.Vacancy;
import com.example.hrinterviewsystem.repository.QuestionRepository;
import com.example.hrinterviewsystem.repository.ResultRepository;
import com.example.hrinterviewsystem.repository.UserRepository;
import com.example.hrinterviewsystem.repository.VacancyRepository;
import com.example.hrinterviewsystem.service.InterviewService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/candidate")
public class CandidateController {

    private final VacancyRepository vacancyRepository;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final InterviewService interviewService;

    public CandidateController(
            VacancyRepository vacancyRepository,
            QuestionRepository questionRepository,
            ResultRepository resultRepository,
            UserRepository userRepository,
            InterviewService interviewService
    ) {
        this.vacancyRepository = vacancyRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.interviewService = interviewService;
    }

    @GetMapping("/panel")
    public String candidatePanel(Model model) {
        model.addAttribute("vacancies", vacancyRepository.findAll());
        return "candidate-panel";
    }

    @GetMapping("/vacancy/{id}")
    public String interviewPage(
            @PathVariable Long id,
            Model model,
            Authentication authentication
    ) {

        Vacancy vacancy = vacancyRepository.findById(id).orElseThrow();

        User user = userRepository.findByLogin(authentication.getName());

        boolean alreadyPassed =
                resultRepository.existsByUserIdAndVacancyId(
                        user.getId(),
                        id
                );

        if (alreadyPassed) {
            model.addAttribute("message", "Вы уже проходили это собеседование");
            return "already-passed";
        }

        List<Question> questions = questionRepository.findByVacancyId(id);

        model.addAttribute("vacancy", vacancy);
        model.addAttribute("questions", questions);

        return "interview";
    }

    @GetMapping("/profile")
    public String profilePage(
            Authentication authentication,
            Model model
    ) {

        User user = userRepository.findByLogin(
                authentication.getName()
        );

        model.addAttribute("user", user);

        return "candidate-profile";
    }

    @GetMapping("/results")
    public String myResults(Authentication authentication, Model model) {

        User user = userRepository.findByLogin(authentication.getName());

        model.addAttribute(
                "results",
                resultRepository.findByUserId(user.getId())
        );

        return "candidate-results";
    }

    @PostMapping("/vacancy/{id}/submit")
    public String submitInterview(
            @PathVariable Long id,
            @RequestParam List<String> answers,
            Authentication authentication,
            Model model
    ) {

        var result = interviewService.processInterview(
                id,
                answers,
                authentication.getName()
        );

        model.addAttribute("score", result.getScore());
        model.addAttribute("status", result.getStatus());

        return "result";
    }

    @PostMapping("/profile")
    public String saveProfile(
            @ModelAttribute User formUser,
            Authentication authentication
    ) {

        User user = userRepository.findByLogin(
                authentication.getName()
        );

        user.setFullName(formUser.getFullName());
        user.setEmail(formUser.getEmail());
        user.setPhone(formUser.getPhone());
        user.setAbout(formUser.getAbout());

        userRepository.save(user);

        return "redirect:/candidate/panel";
    }
}