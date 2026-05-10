package com.koushik.quiz_service.service;

import com.koushik.quiz_service.dao.QuizDao;
import com.koushik.quiz_service.feign.QuizInterface;
import com.koushik.quiz_service.model.QuestionWrapper;
import com.koushik.quiz_service.model.Quiz;
import com.koushik.quiz_service.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    @Override
    public ResponseEntity<String> createQuiz(String categoryName, Integer numOfQuestions, String title) {
        List<Integer> questions = quizInterface.getQuestionsForQuiz(categoryName, numOfQuestions).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionById(questionIds);
        return questions;
    }

    @Override
    public ResponseEntity<Integer> calculateResult(Integer id, List<UserResponse> responses) {
        ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
    }
}
