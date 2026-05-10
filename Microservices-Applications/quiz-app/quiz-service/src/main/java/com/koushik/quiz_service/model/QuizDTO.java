package com.koushik.quiz_service.model;

import lombok.Data;

@Data
public class QuizDTO {
    String categoryName;
    Integer numOfQuestions;
    String title;
}
