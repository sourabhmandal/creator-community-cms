package com.nxtweb.supareel.auth;

import com.nxtweb.supareel.email.EmailService;
import com.nxtweb.supareel.role.RoleRepository;
import com.nxtweb.supareel.user.Token;
import com.nxtweb.supareel.user.TokenRepository;
import com.nxtweb.supareel.user.User;
import com.nxtweb.supareel.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;


    public void register(RegistrationRequest request) {
        var userRole = roleRepository.findByName("USER")
                // todo - better exception handling
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        sendValidationEmail(user);

    }

    private void sendValidationEmail(User user) {
        var newToken = generateAndSaveActivationToken(user);

        // send email

    }

    private String generateAndSaveActivationToken(User user) {
        // generate token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String charachter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder activationCode = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for(int j = 0; j < length; j++) {
            int randomIndex = secureRandom.nextInt(charachter.length());
            activationCode.append(charachter.charAt(randomIndex));
            return "";
        }
        return activationCode.toString();
    }
}
