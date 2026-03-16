package org.example.task_managment_system.exception;

public class JwtException extends RuntimeException{
    public JwtException(String message) {
        super(message);
    }
}
