package ru.logistica.tms.dto;

public class ValidateDataException extends Exception {
    public ValidateDataException() {
    }

    public ValidateDataException(String message) {
        super(message);
    }

    public ValidateDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateDataException(Throwable cause) {
        super(cause);
    }

    public ValidateDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
