package ru.logist.sbat.jsonParser;

public class ValidatorException extends RuntimeException {
    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(String message) {
        super(message);
    }
}
