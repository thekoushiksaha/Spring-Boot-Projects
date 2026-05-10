package com.koushik.quiz_service.controller;

import com.koushik.quiz_service.model.QuestionWrapper;
import com.koushik.quiz_service.model.QuizDTO;
import com.koushik.quiz_service.model.UserResponse;
import com.koushik.quiz_service.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @GetMapping("info")
    public String info() {
        return "Quiz-Service API";
    }

    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestBody QuizDTO quizDto) {
        return quizService.createQuiz(quizDto.getCategoryName(), quizDto.getNumOfQuestions(), quizDto.getTitle());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id){
        return quizService.getQuizQuestions(id);
    }

    @PostMapping("/submit/{id}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable Integer id, @RequestBody List<UserResponse> responses){
        return quizService.calculateResult(id, responses);
    }
}
