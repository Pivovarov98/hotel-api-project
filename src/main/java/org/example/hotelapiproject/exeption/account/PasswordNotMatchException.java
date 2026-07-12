package org.example.hotelapiproject.exeption.account;

public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException(String message) {
        super(message);
    }
}
