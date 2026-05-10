package com.koushik.quiz_app.service;

import com.koushik.quiz_app.dao.QuestionDao;
import com.koushik.quiz_app.dao.QuizDao;
import com.koushik.quiz_app.model.Question;
import com.koushik.quiz_app.model.QuestionWrapper;
import com.koushik.quiz_app.model.Quiz;
import com.koushik.quiz_app.model.UserResponseSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizDao quizDao;
    @Autowired
    private QuestionDao questionDao;

    @Override
    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        if (quiz.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Question> questionFromDB = quiz.get().getQuestions();
        List<QuestionWrapper> questionForUser = new ArrayList<>();
        for (Question question : questionFromDB) {
            QuestionWrapper qw = new QuestionWrapper(question.getId(), question.getQuestion(), question.getOption1(), question.getOption2(), question.getOption3(), question.getOption4());
            questionForUser.add(qw);
        }
        return new ResponseEntity<>(questionForUser, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> calculateResult(Integer id, List<UserResponseSheet> userResponseSheets) {
        Optional<Quiz>  quiz = quizDao.findById(id);
        if (quiz.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Question> questions = quiz.get().getQuestions();
        int result = 0;
        int index = 0;
        for(UserResponseSheet response : userResponseSheets){
            if(response.getAnswer().equals(questions.get(index).getAnswer())){
                result++;
            }
            index++;
        }
        String score = result + "/" + questions.size();
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
}
