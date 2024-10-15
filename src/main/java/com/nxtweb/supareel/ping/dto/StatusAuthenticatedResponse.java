package com.nxtweb.supareel.ping.dto;

import lombok.*;
import org.springframework.security.core.Authentication;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusAuthenticatedResponse {
    private String status;
    private Authentication user;
}
