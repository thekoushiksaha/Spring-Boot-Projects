package org.koushik.jwtsecurityrefreshtoken01.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
