package ru.logist.sbat.jsonParser;

import org.json.simple.JSONObject;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Objects;

public class Util {
    public static void withValidatorExceptionRedirect(Runnable run) {
        try {
            run.run();
        } catch (Exception e) {
            throw new ValidatorException(e.getMessage(), e);
        }
    }

    public static void requireNonNull(Object obj, String fieldName, JSONObject jsonObject) {
        withValidatorExceptionRedirect(() -> Objects.requireNonNull(obj, getParameterizedString("{} must not be null in {}", fieldName, jsonObject)));
    }

    // TODO remove
    public static void requireNonNull(Object obj, String fieldName) {
        withValidatorExceptionRedirect(() -> Objects.requireNonNull(obj, getParameterizedString("{} must not be null", fieldName)));
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

    public static String getParameterizedString(String string, Object... parameters) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] parts = string.split("\\{\\}", -1);
        if ((parts.length - 1) != parameters.length)
            throw new IllegalArgumentException("number of {} not equal to number of parameters");
        stringBuilder.append(parts[0]);
        for (int i = 0; i < parameters.length; i++) {
            stringBuilder.append("[").append(parameters[i]).append("]").append(parts[i + 1]);
        }
        return stringBuilder.toString();
    }

    public static void checkFieldAvailability(String fieldName, JSONObject jsonObject) {
        if (!jsonObject.containsKey(fieldName))
            throw new ValidatorException(Util.getParameterizedString("can't find field {} in {}", fieldName, jsonObject));
    }

    public static void checkCorrectType(Object fieldContent, Class fieldContentClazz, JSONObject jsonObject) {
        if (!fieldContent.getClass().equals(fieldContentClazz))
            throw new ValidatorException(Util.getParameterizedString("fieldContent {} is not type of {} in {} ", fieldContent, fieldContentClazz.getSimpleName(), jsonObject));
    }


    public static void checkFieldAvailableAndNotNull(String fieldName, JSONObject jsonObject) {
        checkFieldAvailability(fieldName, jsonObject);
        requireNonNull(jsonObject.get(fieldName), fieldName, jsonObject);
    }
}
