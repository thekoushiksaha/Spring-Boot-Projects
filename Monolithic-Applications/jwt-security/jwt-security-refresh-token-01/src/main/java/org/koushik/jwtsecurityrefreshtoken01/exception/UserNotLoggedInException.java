package org.koushik.jwtsecurityrefreshtoken01.exception;

public class UserNotLoggedInException extends RuntimeException{
    public UserNotLoggedInException(String message){
        super(message);
    }
}
