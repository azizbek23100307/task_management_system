package org.example.task_managment_system.exception;

import org.example.task_managment_system.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(ResourceNotFoundException ex) {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.NOT_FOUND)
                .body(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(BadRequestException ex) {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .body(null)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<ApiResponse> handleForbidden(ForbiddenOperationException ex) {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.FORBIDDEN)
                .body(null)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ApiResponse response = ApiResponse.builder()
                .message(message)
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .body(null)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleOther(Exception ex) {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
