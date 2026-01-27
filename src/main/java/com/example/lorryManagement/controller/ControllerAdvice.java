package com.example.lorryManagement.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<?> handleDuplicateKeyException(
            DuplicateKeyException e,
            HttpServletRequest request
    ) {
        log.warn("Duplicate key violation: {}", e.getMessage());

        return ResponseEntity.status(409).body(
                errorBody(
                        409,
                        "Conflict",
                        e.getMessage(),
                        request
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        List<Map<String, String>> errors =
                e.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(fe -> Map.of(
                                "field", fe.getField(),
                                "message", fe.getDefaultMessage()
                        ))
                        .toList();

        Map<String, Object> body =
                errorBody(400, "Bad Request", "Validation failed", request);

        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            IllegalArgumentException e,
            HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(
                errorBody(
                        400,
                        "Bad Request",
                        e.getMessage(),
                        request
                )
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(
            NoSuchElementException e,
            HttpServletRequest request
    ) {
        log.warn("Resource not found: {}", e.getMessage());

        return ResponseEntity.status(404).body(
                errorBody(
                        404,
                        "Not Found",
                        e.getMessage(),
                        request
                )
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(
            DataIntegrityViolationException e,
            HttpServletRequest request
    ) {
        log.error("Data integrity violation", e);

        return ResponseEntity.status(409).body(
                errorBody(
                        409,
                        "Conflict",
                        "Database constraint violated",
                        request
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnhandledException(
            Exception e,
            HttpServletRequest request
    ) {
        log.error("Unhandled exception", e);

        return ResponseEntity.status(500).body(
                errorBody(
                        500,
                        "Internal Server Error",
                        "Something went wrong. Please try again later.",
                        request
                )
        );
    }

    private Map<String, Object> errorBody(
            int status,
            String error,
            String message,
            HttpServletRequest request
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("path", request.getRequestURI());
        return body;
    }

}
