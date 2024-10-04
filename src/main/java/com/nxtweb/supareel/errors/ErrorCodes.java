package com.nxtweb.supareel.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCodes {

    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Login and / or Password is incorrect"),
    VALIDATION_ERROR(305, BAD_REQUEST, "payload not valid"),
    AUTHENTICATION_ERROR(306, UNAUTHORIZED, "Authentication error occurred"),
    UNKNOWN_ERROR(307, INTERNAL_SERVER_ERROR, "Unknown / Unhandled error occurred"),
    DATABASE_ERROR(308, CONFLICT, "Unknown database error occurred"),
    DATABASE_DUPLICATE_KEY_ERROR(309, CONFLICT, "A record with the same key already exists. Please use a different value"),
    DATABASE_ENTITY_NOT_FOUND(310, NOT_FOUND, "The requested entity does not exist in the database"),
    DATABASE_QUERY_TIMEOUT(311, REQUEST_TIMEOUT, "The database query took too long to execute and timed out. Please try again"),
    DATABASE_FOREIGN_KEY_ERROR(312, CONFLICT, "The operation violates a foreign key constraint. Please ensure the related entity exists"),
    DATABASE_MISSING_FIELD_ERROR(313, BAD_REQUEST, "A required field is missing. Please ensure all required fields are filled"),
    ;


    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    ErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
