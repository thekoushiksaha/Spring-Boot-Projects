package com.koushik.quiz_app.service;

import com.koushik.quiz_app.model.Question;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuestionService {
    ResponseEntity<List<Question>> getAllQuestions();

    ResponseEntity<List<Question>> getAllQuestionsByCategory(String category);

    ResponseEntity<Question> saveQuestion(Question question);

    ResponseEntity<Integer> deleteQuestion(Integer id);

    ResponseEntity<Question> updateQuestion(Question question);
}
