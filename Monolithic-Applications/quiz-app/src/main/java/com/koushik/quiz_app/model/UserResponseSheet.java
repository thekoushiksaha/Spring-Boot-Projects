package com.koushik.quiz_app.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserResponseSheet {
    private Integer id;
    private String answer;
}
