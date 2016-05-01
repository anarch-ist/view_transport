package ru.logist.sbat.jsonParser.jsonReader;

/**
 * If this kind of exception was thrown it means that Json file is not in accordance with JsonConstraints.txt
 */
public class ValidatorException extends Exception {
    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(String message) {
        super(message);
    }
}
