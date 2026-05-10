package com.koushik.question_service.service;

import com.koushik.question_service.dao.QuestionDao;
import com.koushik.question_service.model.Question;
import com.koushik.question_service.model.QuestionWrapper;
import com.koushik.question_service.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionDao questionDao;



    @Override
    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Question>> getAllQuestionsByCategory(String category) {
        try {
            if (!questionDao.existsByCategory(category)) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(questionDao.findAllQuestionsByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Question> saveQuestion(Question question) {
        try {
            return new ResponseEntity<>(questionDao.save(question), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Integer> deleteQuestion(Integer id) {
        try {
            if (!questionDao.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            questionDao.deleteById(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Question> updateQuestion(Question question) {
        try {
            if (!questionDao.existsById(question.getId())) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(questionDao.save(question), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Question(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, Integer numOfQuestion) {
        try {
            if (!questionDao.existsByCategory(category)) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(questionDao.findRandomQuestionsByCategory(category, numOfQuestion), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<List<QuestionWrapper>> getQuestionsById(List<Integer> ids) {
        try {
            List<QuestionWrapper> wrappers = new ArrayList<>();
            List<Question> questions = new ArrayList<>();
            List<QuestionWrapper> notFoundWrappers = new ArrayList<>();
            for (Integer id : ids) {
                if (questionDao.findById(id).isEmpty()) {
                    QuestionWrapper wrapper = new QuestionWrapper();
                    wrapper.setId(id);
                    notFoundWrappers.add(wrapper);
                } else {
                    questions.add(questionDao.findById(id).get());
                }
            }

            for (Question question : questions) {
                QuestionWrapper wrapper = new QuestionWrapper();
                wrapper.setId(question.getId());
                wrapper.setQuestion(question.getQuestion());
                wrapper.setOption1(question.getOption1());
                wrapper.setOption2(question.getOption2());
                wrapper.setOption3(question.getOption3());
                wrapper.setOption4(question.getOption4());

                wrappers.add(wrapper);
            }

            if (!notFoundWrappers.isEmpty()) {
                return new ResponseEntity<>(notFoundWrappers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(wrappers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public ResponseEntity<Integer> getScore(List<UserResponse> responses) {
        try {
            int right = 0;
            for (UserResponse response : responses) {
                if (questionDao.findById(response.getId()).isPresent()) {
                    Question question = questionDao.findById(response.getId()).get();
                    if (response.getAnswer().equals(question.getAnswer())) {
                        right++;
                    }
                } else {
                    return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
                }
            }
            return new ResponseEntity<>(right, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
