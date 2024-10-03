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
public class RegistrationResponse {
    @NotEmpty(message = "Status is mandatory")
    @NotBlank(message = "Status is mandatory")
    private String status;

    @NotEmpty(message = "Message is mandatory")
    @NotBlank(message = "Message is mandatory")
    private String message;

}
