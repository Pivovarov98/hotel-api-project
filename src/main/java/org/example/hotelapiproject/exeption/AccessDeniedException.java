package org.example.hotelapiproject.exeption;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String msg) {
        super(msg);
    }

    public AccessDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
