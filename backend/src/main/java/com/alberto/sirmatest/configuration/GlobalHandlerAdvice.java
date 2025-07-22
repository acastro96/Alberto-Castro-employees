package com.alberto.sirmatest.configuration;

import com.alberto.sirmatest.exception.CompanyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalHandlerAdvice {

  private static final String ERROR_TYPE = "errorType";
  private static final String MESSAGE = "message";
  private static final String TIMESTAMP = "timestamp";

  private static final Logger log = LoggerFactory.getLogger(GlobalHandlerAdvice.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage())
    );
    log.error("Validation errors", ex);
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(CompanyException.class)
  public ResponseEntity<Object> handleBusinessError(CompanyException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put(ERROR_TYPE, "Business");
    body.put(MESSAGE, ex.getMessage());
    body.put(TIMESTAMP, LocalDateTime.now());

    log.error("Business exception", ex);

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleUnreadableException(HttpMessageNotReadableException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put(ERROR_TYPE, "Fatal");
    body.put(MESSAGE, "Malformed or missing request body, please verify.");
    body.put(TIMESTAMP, LocalDateTime.now());
    log.error("Unreadable exception, Malformed or missing request body", ex);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGenericError(Exception ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put(ERROR_TYPE, "Fatal");
    body.put(MESSAGE, "Fatal error, please see the logs and contact to support ".concat(ex.getMessage()));
    body.put(TIMESTAMP, LocalDateTime.now());
    log.error("Fatal error", ex);
    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
