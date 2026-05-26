package com.example.hrinterviewsystem.service;

import com.example.hrinterviewsystem.entity.*;
import com.example.hrinterviewsystem.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewService {

    private final VacancyRepository vacancyRepository;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;

    public InterviewService(
            VacancyRepository vacancyRepository,
            QuestionRepository questionRepository,
            ResultRepository resultRepository,
            UserRepository userRepository
    ) {
        this.vacancyRepository = vacancyRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
    }

    public Result processInterview(Long vacancyId,
                                   List<String> answers,
                                   String username) {

        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow();

        List<Question> questions =
                questionRepository.findByVacancyId(vacancyId);

        int totalScore = 0;
        int maxScore = 0;

        for (int i = 0; i < questions.size(); i++) {

            Question q = questions.get(i);

            maxScore += q.getWeight();

            String correct = q.getCorrectAnswer()
                    .trim()
                    .toLowerCase();

            String userAnswer = answers.get(i)
                    .trim()
                    .toLowerCase();

            if (correct.equals(userAnswer)) {
                totalScore += q.getWeight();
            }
        }

        double percent = (maxScore == 0)
                ? 0
                : ((double) totalScore / maxScore) * 100;

        String status;

        if (percent >= 80) {
            status = "Подходит";
        } else if (percent >= 50) {
            status = "Возможно подходит";
        } else {
            status = "Не подходит";
        }

        User user = userRepository.findByLogin(username);

        Result result = new Result();
        result.setUser(user);
        result.setVacancy(vacancy);
        result.setScore(percent);
        result.setStatus(status);

        return resultRepository.save(result);
    }
}