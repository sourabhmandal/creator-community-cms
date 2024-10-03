package com.nxtweb.supareel.auth;

import com.nxtweb.supareel.errors.DatabaseOperationException;
import com.nxtweb.supareel.errors.ErrorResponse;
import com.nxtweb.supareel.errors.RoleNotFoundException;
import com.nxtweb.supareel.errors.UserAlreadyExistsException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RegistrationResponse> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        service.register(request);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(RegistrationResponse
                        .builder()
                        .status("SUCCESS")
                        .message(String.format("User with email %s registered successfully", request.getEmail()))
                        .build()
                );
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) throws MessagingException {
        return ResponseEntity.ok(service.login(request));
    }

    @GetMapping("/activate-account")
    public ResponseEntity<ActivateAccountResponse> authenticate(@RequestParam String token) throws MessagingException {
        return ResponseEntity.ok(service.activateAccount(token));
    }

    // Handle any validation errors that occur during request body validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    // Handle custom exceptions related to registration or authentication
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationExceptions(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse
                        .builder()
                        .status("ERROR")
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse
                        .builder()
                        .status("ERROR")
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> handleRoleNotFoundException(RoleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse
                        .builder()
                        .status("ERROR")
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<?> handleDatabaseOperationException(DatabaseOperationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse
                        .builder()
                        .status("ERROR")
                        .message(ex.getMessage())
                        .build()
        );
    }


    // Handle database errors like unique constraint violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDatabaseExceptions(DataIntegrityViolationException ex) {
        String errorMessage = extractConstraintViolationMessage(ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse
                    .builder()
                    .status("ERROR")
                    .message(errorMessage)
                    .build()
        );
    }

    // Extract a user-friendly error message from the exception
    private String extractConstraintViolationMessage(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            String detailMessage = ((org.hibernate.exception.ConstraintViolationException) ex.getCause()).getSQLException().getMessage();
            if (detailMessage.contains("duplicate key value")) {
                return "A record with the same value already exists. Please use a different value.";
            }
        }
        return "Database error occurred. Please try again.";
    }

    // Handle database errors like unique constraint violations
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse
                        .builder()
                        .status("ERROR")
                        .message(ex.getMessage())
                        .build()
        );
    }

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralExceptions(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse
                        .builder()
                        .status("ERROR")
                        .message("An unexpected error occurred.")
                        .build()
                );
    }
}
