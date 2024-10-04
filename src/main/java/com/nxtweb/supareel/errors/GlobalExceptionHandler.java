package com.nxtweb.supareel.errors;

import jakarta.mail.MessagingException;
import jakarta.persistence.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.*;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.util.*;

import static com.nxtweb.supareel.errors.ErrorCodes.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handle any validation errors that occur during request body validation
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleLockedExceptions(LockedException ex) {
        return ResponseEntity.status(ACCOUNT_LOCKED.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .code(ACCOUNT_LOCKED.getCode())
                        .description(ACCOUNT_LOCKED.getDescription())
                        .error(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleDisabledExceptions(DisabledException ex) {
        return ResponseEntity.status(ACCOUNT_DISABLED.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .code(ACCOUNT_DISABLED.getCode())
                        .description(ACCOUNT_DISABLED.getDescription())
                        .error(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsExceptions(BadCredentialsException ex) {
        return ResponseEntity.status(BAD_CREDENTIALS.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .code(BAD_CREDENTIALS.getCode())
                        .description(BAD_CREDENTIALS.getDescription())
                        .error(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleMessagingExceptions(MessagingException ex) {
        return ResponseEntity.internalServerError().body(
                ExceptionResponse.builder()
                        .error(ex.getMessage())
                        .build()
        );
    }

    // Handle any validation errors that occur during request body validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Set<String> errors = new HashSet<>();

        ex.getBindingResult().getAllErrors().forEach(error ->
                errors.add(error.getDefaultMessage())
        );

        return ResponseEntity.status(VALIDATION_ERROR.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .code(VALIDATION_ERROR.getCode())
                        .description(VALIDATION_ERROR.getDescription())
                        .error(ex.getMessage())
                        .validationErrors(errors)
                        .build()
        );
    }

    // Handle custom exceptions related to registration or authentication
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationExceptions(AuthenticationException ex) {
        return ResponseEntity.status(AUTHENTICATION_ERROR.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .code(AUTHENTICATION_ERROR.getCode())
                        .description(AUTHENTICATION_ERROR.getDescription())
                        .error(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ExceptionResponse> handleDatabaseOperationException(DatabaseOperationException ex) {
        return ResponseEntity.status(DATABASE_ERROR.getCode()).body(
                ExceptionResponse
                        .builder()
                        .code(DATABASE_ERROR.getCode())
                        .description(DATABASE_ERROR.getDescription())
                        .error(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateKeyException(DuplicateKeyException ex) {
        return ResponseEntity.status(DATABASE_DUPLICATE_KEY_ERROR.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .code(DATABASE_DUPLICATE_KEY_ERROR.getCode())
                        .description(DATABASE_DUPLICATE_KEY_ERROR.getDescription())
                        .error(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(DATABASE_ENTITY_NOT_FOUND.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .code(DATABASE_ENTITY_NOT_FOUND.getCode())
                        .description(DATABASE_ENTITY_NOT_FOUND.getDescription())
                        .error(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<ExceptionResponse> handleQueryTimeout(QueryTimeoutException ex) {
        return ResponseEntity.status(DATABASE_QUERY_TIMEOUT.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .code(DATABASE_QUERY_TIMEOUT.getCode())
                        .description(DATABASE_QUERY_TIMEOUT.getDescription())
                        .error(ex.getMessage())
                        .build()
        );
    }

    // Handle database errors like unique constraint violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDatabaseExceptions(DataIntegrityViolationException ex) {
        ErrorCodes errorMessage = extractConstraintViolationMessage(ex);
        return ResponseEntity.status(errorMessage.getHttpStatus()).body(
                ExceptionResponse
                        .builder()
                        .code(errorMessage.getCode())
                        .description(errorMessage.getDescription())
                        .error(ex.getMessage())
                        .build()
        );
    }

    // Extract a user-friendly error message from the exception
    private ErrorCodes extractConstraintViolationMessage(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            String detailMessage = ((ConstraintViolationException) ex.getCause()).getSQLException().getMessage();
            if (detailMessage.contains("duplicate key value")) {
                return DATABASE_DUPLICATE_KEY_ERROR;
            } else if (detailMessage.contains("violates foreign key constraint")) {
                return DATABASE_FOREIGN_KEY_ERROR;
            } else if (detailMessage.contains("cannot be null")) {
                return DATABASE_MISSING_FIELD_ERROR;
            }
        }
        return DATABASE_ERROR;
    }

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralExceptions(Exception ex) {
        ex.printStackTrace();

        return ResponseEntity
                .status(UNKNOWN_ERROR.getHttpStatus())
                .body(ExceptionResponse
                        .builder()
                        .code(UNKNOWN_ERROR.getCode())
                        .description(UNKNOWN_ERROR.getDescription())
                        .error(ex.getMessage())
                        .build()
                );
    }
}
