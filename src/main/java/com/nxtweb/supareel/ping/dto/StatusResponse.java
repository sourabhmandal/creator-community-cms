package com.nxtweb.supareel.ping.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusResponse {
    private String status;
    private String message;
}
