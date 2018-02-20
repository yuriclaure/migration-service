package com.migration.service.exceptions;

public class MigrationFailureException extends RuntimeException {

    public MigrationFailureException() {
    }

    public MigrationFailureException(String message) {
        super(message);
    }

    public MigrationFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
