package com.example.lorryManagement.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {
    @ExceptionHandler (DuplicateKeyException.class)
    public ResponseEntity<Map<String,Object>> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        log.error("DB integrity violation", e);
        String root = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : e.getMessage();

        Matcher m = Pattern.compile("Duplicate entry '(.+?)'").matcher(root == null ? "" : root);
        String message = m.find() ? "LR " + m.group(1) + " already exists" : "Unique constraint violated";

        Map<String,Object> body = new HashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", 409);
        body.put("error","Conflict");
        body.put("message", message);
        body.put("path", request.getRequestURI());

        log.info("Returning 409 body: {}", body);

        return ResponseEntity.status(409)
                .header("Content-Type", "application/json")
                .body(body);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<Map<String, String>> errors = fieldErrors.stream().map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage())).collect(Collectors.toList());

        Map<String,Object> body = new HashMap<>();

        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", "Validation failed");
        body.put("errors", errors);
        body.put("path", request.getRequestURI());

        return ResponseEntity.status(400).body(body);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e, HttpServletRequest request) {
        log.warn("Resource not found: {}", e.getMessage());
        Map<String,Object> body = new HashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", e.getMessage());
        body.put("path", request.getRequestURI());
        return ResponseEntity.status(404).body(body);
    }

}
