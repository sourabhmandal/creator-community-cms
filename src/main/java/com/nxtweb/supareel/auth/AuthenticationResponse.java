package com.nxtweb.supareel.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {

    @NotEmpty(message = "Token is mandatory")
    @NotBlank(message = "Token name is mandatory")
    private String token;
}
