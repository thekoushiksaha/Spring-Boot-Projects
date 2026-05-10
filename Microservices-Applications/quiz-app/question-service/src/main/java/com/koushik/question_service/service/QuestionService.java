package com.koushik.question_service.service;

import com.koushik.question_service.model.Question;
import com.koushik.question_service.model.QuestionWrapper;
import com.koushik.question_service.model.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuestionService {
    ResponseEntity<List<Question>> getAllQuestions();

    ResponseEntity<List<Question>> getAllQuestionsByCategory(String category);

    ResponseEntity<Question> saveQuestion(Question question);

    ResponseEntity<Integer> deleteQuestion(Integer id);

    ResponseEntity<Question> updateQuestion(Question question);

    ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, Integer numOfQuestion);

    ResponseEntity<List<QuestionWrapper>> getQuestionsById(List<Integer> ids);

    ResponseEntity<Integer> getScore(List<UserResponse> responses);
}
