package com.nxtweb.supareel.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ActivateAccountResponse {

    @NotEmpty(message = "Status is mandatory")
    @NotBlank(message = "Status name is mandatory")
    private String status;

    @NotEmpty(message = "Message is mandatory")
    @NotBlank(message = "Message name is mandatory")
    private String message;

}
