package com.koushik.quiz_app.controller;

import com.koushik.quiz_app.model.Question;
import com.koushik.quiz_app.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/info")
    public String getAPIInfo() {
        return "Question API";
    }

    @GetMapping("/getAllQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Question>> getAllQuestionsByCategory(@PathVariable String category) {
        return questionService.getAllQuestionsByCategory(category);
    }

    @PostMapping("/saveQuestion")
    public ResponseEntity<Question> saveQuestion(@RequestBody Question question) {
        return questionService.saveQuestion(question);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Integer> deleteQuestionById(@PathVariable("id") Integer id) {
        return questionService.deleteQuestion(id);
    }

    @PutMapping("/updateQuestion")
    public ResponseEntity<Question> updateQuestion(@RequestBody Question question) {
        return questionService.updateQuestion(question);
    }
}
