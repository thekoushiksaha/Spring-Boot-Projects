package com.koushik.quiz_service.feign;

import com.koushik.quiz_service.model.QuestionWrapper;
import com.koushik.quiz_service.model.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("QUESTION-SERVICE")
public interface QuizInterface {
    @GetMapping("/question/generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category, @RequestParam Integer numOfQuestion);

    @PostMapping("/question/getQuestionsById")
    public ResponseEntity<List<QuestionWrapper>> getQuestionById(@RequestBody List<Integer> ids);

    @PostMapping("/question/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<UserResponse> responses);
}
