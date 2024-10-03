package com.nxtweb.supareel.auth;

import com.nxtweb.supareel.email.EmailService;
import com.nxtweb.supareel.email.EmailTemplateName;
import com.nxtweb.supareel.errors.DatabaseOperationException;
import com.nxtweb.supareel.errors.RoleNotFoundException;
import com.nxtweb.supareel.errors.UserAlreadyExistsException;
import com.nxtweb.supareel.role.RoleRepository;
import com.nxtweb.supareel.security.JwtService;
import com.nxtweb.supareel.user.Token;
import com.nxtweb.supareel.user.TokenRepository;
import com.nxtweb.supareel.user.User;
import com.nxtweb.supareel.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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
        } catch (MessagingException e) {
            throw new MessagingException("Error while sending email to user: " + e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseOperationException("Error while saving user: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while saving user: " + e.getMessage(), e);
        }

    }

    public AuthenticationResponse login(LoginRequest request) throws MessagingException {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = (User) auth.getPrincipal();
        var claims = new HashMap<String, Object>();
        claims.put("email", user.getEmail());
        claims.put("fullName", user.getFullName());
        claims.put("roles", user.getRoles());

        var jwtToken = jwtService.generateUserToken(claims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public ActivateAccountResponse activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Token expired: A new token sent to registered email");
        }
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));



        try {
            user.setEnabled(true);
            userRepository.save(user);
            savedToken.setValidatedAt(LocalDateTime.now());
            tokenRepository.save(savedToken);
            return ActivateAccountResponse.builder()
                    .status("STATUS")
                    .message("account activated successfully")
                    .build();
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseOperationException("Error while updating user: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while updating user: " + e.getMessage(), e);
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
        try {
            // find old tokens
            var tokens = tokenRepository.findAllActiveTokenByUserId(user.getId());
            if (!tokens.isEmpty()) {
                tokens.forEach(token -> token.setValidatedAt(LocalDateTime.now())); // Mark them as expired
                tokenRepository.saveAll(tokens); // Save the updated tokens
            }

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
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseOperationException("Error while saving activation token: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseOperationException("An unexpected error occurred while saving user: " + e.getMessage(), e);
        }
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
