package ru.logist.sbat.jsonParser;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Util {
    public static void withValidatorExceptionRedirect(Runnable run) {
        try {
            run.run();
        } catch (Exception e) {
            throw new ValidatorException(e.getMessage(), e);
        }
    }

    public static void requireNonNull(Object obj, String fieldName) {
        withValidatorExceptionRedirect(() -> Objects.requireNonNull(obj, "[" + fieldName + "] must not be null"));
    }

    public static void requireNonNullOrEmpty(String string, String fieldName) {
        withValidatorExceptionRedirect(() -> {
            Objects.requireNonNull(string, "[" + fieldName + "] must not be null");
            if (string.isEmpty())
                throw new IllegalArgumentException("[" + fieldName + "] must not be empty");
        });
    }

    public static Date getDateWithCheck(String dateAsString, String fieldName, DateTimeFormatter formatter) {
        final Date[] result = {null};
        withValidatorExceptionRedirect(() -> {
            Objects.requireNonNull(dateAsString, fieldName);
            if (dateAsString.isEmpty())
                result[0] = null;
            else
                result[0] = Date.valueOf(LocalDate.parse(dateAsString, formatter));
        });
        return result[0];
    }
}
