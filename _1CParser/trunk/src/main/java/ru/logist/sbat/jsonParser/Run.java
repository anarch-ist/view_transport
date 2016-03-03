package ru.logist.sbat.jsonParser;

import java.util.Objects;

public class Run {
    public static void withValidatorExceptionRedirect(Runnable run) {
        try {
            run.run();
        } catch (Exception e) {
            throw new ValidatorException(e.getMessage(), e);
        }
    }

    public static void requireNonNull(String string, String fieldName) {
        withValidatorExceptionRedirect(() -> Objects.requireNonNull(string, "[" + fieldName + "] must not be null"));
    }

    public static void requireNonNullOrEmpty(String string, String fieldName) {
        withValidatorExceptionRedirect(() -> {
            Objects.requireNonNull(string, "[" + fieldName + "] must not be null");
            if (string.isEmpty())
                throw new IllegalArgumentException("[" + fieldName + "] must not be empty");
        });
    }

}
