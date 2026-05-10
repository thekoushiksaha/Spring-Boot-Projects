package com.koushik.quiz_app.service;

import com.koushik.quiz_app.model.QuestionWrapper;
import com.koushik.quiz_app.model.UserResponseSheet;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuizService {
    ResponseEntity<String> createQuiz(String category, int numQ, String title);

    ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id);

    ResponseEntity<String> calculateResult(Integer id, List<UserResponseSheet> userResponseSheets);
}
