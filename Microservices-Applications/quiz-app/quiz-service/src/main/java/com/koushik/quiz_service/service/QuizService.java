package com.koushik.quiz_service.service;

import com.koushik.quiz_service.model.QuestionWrapper;
import com.koushik.quiz_service.model.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuizService {
    ResponseEntity<String> createQuiz(String categoryName, Integer numOfQuestions, String title);

    ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id);

    ResponseEntity<Integer> calculateResult(Integer id, List<UserResponse> responses);
}
