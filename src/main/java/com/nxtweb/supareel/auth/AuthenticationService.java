package com.nxtweb.supareel.auth;

import com.nxtweb.supareel.email.EmailService;
import com.nxtweb.supareel.email.EmailTemplateName;
import com.nxtweb.supareel.errors.DatabaseOperationException;
import com.nxtweb.supareel.errors.RoleNotFoundException;
import com.nxtweb.supareel.errors.UserAlreadyExistsException;
import com.nxtweb.supareel.role.RoleRepository;
import com.nxtweb.supareel.user.Token;
import com.nxtweb.supareel.user.TokenRepository;
import com.nxtweb.supareel.user.User;
import com.nxtweb.supareel.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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

    @Value("${application.security.mailing.frontend.activation-url}")
    private String activationUrl;


    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                // todo - better exception handling
                .orElseThrow(() -> new RoleNotFoundException("ROLE USER was not initialized"));

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(
                    String.format("User with email %s already exists", request.getEmail())
            );
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        try {
            userRepository.save(user);
            sendValidationEmail(user);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseOperationException("Error while saving user: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseOperationException("An unexpected error occurred while saving user: " + e.getMessage(), e);
        }

    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        // send email
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"
        );
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



        try {
            tokenRepository.save(token);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseOperationException("Error while saving activation token: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseOperationException("An unexpected error occurred while saving user: " + e.getMessage(), e);
        }
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String character = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder activationCode = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for(int j = 0; j < length; j++) {
            int randomIndex = secureRandom.nextInt(character.length());
            activationCode.append(character.charAt(randomIndex));
        }
        return activationCode.toString();
    }
}
