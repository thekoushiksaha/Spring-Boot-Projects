package com.koushik.question_service.controller;

import com.koushik.question_service.model.Question;
import com.koushik.question_service.model.QuestionWrapper;
import com.koushik.question_service.model.UserResponse;
import com.koushik.question_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    Environment environment;


    @GetMapping("/info")
    public String getAPIInfo() {
        return "Question-Service API";
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

    @GetMapping("/generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category, @RequestParam Integer numOfQuestion) {
        return questionService.getQuestionsForQuiz(category,numOfQuestion);
    }

    @PostMapping("/getQuestionsById")
    public ResponseEntity<List<QuestionWrapper>> getQuestionById(@RequestBody List<Integer> ids) {
        System.out.println(environment.getProperty("local.server.port"));
        return questionService.getQuestionsById(ids);
    }

    @PostMapping("/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<UserResponse> responses){
        return questionService.getScore(responses);
    }
}
