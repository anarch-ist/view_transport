package ru.logist.sbat;

public class GlobalUtils {

    public static final int MAX_SYMBOLS_IN_PARAM_STRING = 500;

    public static String getParameterizedString(String string, Object... parameters) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] parts = string.split("\\{\\}", -1);
        if ((parts.length - 1) != parameters.length)
            throw new IllegalArgumentException("number of {} not equal to number of parameters");
        stringBuilder.append(parts[0]);
        for (int i = 0; i < parameters.length; i++) {
            String paramAsString = parameters[i].toString();
            if (paramAsString.length() > MAX_SYMBOLS_IN_PARAM_STRING)
                paramAsString = paramAsString.substring(0, MAX_SYMBOLS_IN_PARAM_STRING) + "...";
            stringBuilder.append("[").append(paramAsString).append("]").append(parts[i + 1]);
        }
        return stringBuilder.toString();
    }
}
