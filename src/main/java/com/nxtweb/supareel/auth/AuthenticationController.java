package com.nxtweb.supareel.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
