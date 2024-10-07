package com.nxtweb.supareel.common;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    private Status status;
    private String message;

    public enum Status {
        SUCCESS,
        ERROR,
        INFO,
        WARNING;
    }
}