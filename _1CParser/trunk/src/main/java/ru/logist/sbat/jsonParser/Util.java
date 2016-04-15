package ru.logist.sbat.jsonParser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Util {

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

    public static void checkFieldAvailability(String fieldName, JSONObject jsonObject) throws ValidatorException {
        if (!jsonObject.containsKey(fieldName))
            throw new ValidatorException(Util.getParameterizedString("can't find field {} in {}", fieldName, jsonObject));
    }

    public static void checkCorrectType(Object fieldContent, Class fieldContentClazz, JSONObject jsonObject) throws ValidatorException {
        if (!fieldContent.getClass().equals(fieldContentClazz))
            throw new ValidatorException(Util.getParameterizedString("fieldContent {} is not type of {} in {} ", fieldContent, fieldContentClazz.getSimpleName(), jsonObject));
    }

    public static void checkCorrectType(Object fieldContent, Class fieldContentClazz) throws ValidatorException {
        if (!fieldContent.getClass().equals(fieldContentClazz))
            throw new ValidatorException(Util.getParameterizedString("fieldContent {} is not type of {}", fieldContent, fieldContentClazz.getSimpleName()));
    }

    public static void checkFieldAvailableAndNotNull(String fieldName, JSONObject jsonObject) throws ValidatorException {
        checkFieldAvailability(fieldName, jsonObject);
        if (jsonObject.get(fieldName) == null)
            throw new ValidatorException(Util.getParameterizedString("field {} is null in {}", fieldName, jsonObject));
    }

    public static void checkFieldAvailableAndNotNullAndNotEmpty(String fieldName, JSONObject jsonObject) throws ValidatorException {
        checkFieldAvailableAndNotNull(fieldName, jsonObject);
        if (jsonObject.get(fieldName).equals(""))
            throw new ValidatorException((Util.getParameterizedString("field {} is empty in {}", fieldName, jsonObject)));
    }

    /**
     * Long value must be >= 0
     * @param fieldName
     * @param jsonObject
     * @param beanObject
     * @param beanObjectFieldName
     */
    public static void setNullIfEmptyOrSetValueLong(String fieldName, JSONObject jsonObject, Object beanObject, String beanObjectFieldName) throws ValidatorException {
        try {
            Field field = beanObject.getClass().getDeclaredField(beanObjectFieldName);
            field.setAccessible(true);
            Object jsonValue = jsonObject.get(fieldName);
            if (jsonValue.equals(""))
                field.set(beanObject, null);
            else {
                Util.checkCorrectType(jsonValue, Long.class, jsonObject);
                if (((Long)jsonValue) < 0)
                    throw new ValidatorException(Util.getParameterizedString("value of {} must be greater or equal to '0' in {} ", fieldName, jsonObject));
                field.set(beanObject, jsonValue);
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static void setNullIfEmptyOrSetValueDate(String fieldName, JSONObject jsonObject, Object beanObject, String beanObjectFieldName, DateTimeFormatter formatter) throws ValidatorException {
        try {
            Field field = beanObject.getClass().getDeclaredField(beanObjectFieldName);
            field.setAccessible(true);
            Object dateAsString = jsonObject.get(fieldName);
            if (dateAsString.equals(""))
                field.set(beanObject, null);
            else {
                Util.checkCorrectType(dateAsString, String.class, jsonObject);
                try {
                    Date date = Date.valueOf(LocalDate.parse((String) dateAsString, formatter));
                    field.set(beanObject, date);
                } catch (DateTimeParseException e) {
                    throw new ValidatorException(Util.getParameterizedString("illegal date format {} in {}", dateAsString, jsonObject), e);
                }
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static void setStringValue(String fieldName, JSONObject jsonObject, Object beanObject, String beanObjectFieldName) throws ValidatorException {
        try {
            Field field = beanObject.getClass().getDeclaredField(beanObjectFieldName);
            field.setAccessible(true);
            Object jsonValue = jsonObject.get(fieldName);
            Util.checkCorrectType(jsonValue, String.class, jsonObject);
            field.set(beanObject, jsonValue);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static void setStringValue(String fieldName, JSONObject jsonObject, Object beanObject, String beanObjectFieldName, Set<String> possibleValues) throws ValidatorException {
        try {
            Field field = beanObject.getClass().getDeclaredField(beanObjectFieldName);
            field.setAccessible(true);
            Object jsonValue = jsonObject.get(fieldName);
            Util.checkCorrectType(jsonValue, String.class, jsonObject);
            String jsonValueAsString = (String) jsonValue;
            if (!possibleValues.contains(jsonValueAsString))
                throw new ValidatorException(Util.getParameterizedString("status {} is not contained in set of possible statuses in {}", jsonValue, jsonObject));
            field.set(beanObject, jsonValue);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static void setStringSetValue(String fieldName, JSONObject jsonObject, Object beanObject, String beanObjectFieldName) throws ValidatorException {
        try {
            Field field = beanObject.getClass().getDeclaredField(beanObjectFieldName);
            field.setAccessible(true);
            Object jsonValue = jsonObject.get(fieldName);
            Util.checkCorrectType(jsonValue, JSONArray.class, jsonObject);
            JSONArray jsonArray = (JSONArray) jsonValue;

            Set<String> result = new HashSet<>();
            for(Object o: jsonArray) {
                if (o == null || !(o instanceof String) || o.equals("")) {
                    throw new ValidatorException(Util.getParameterizedString("illegal invoiceId {} in {}", o, jsonObject));
                }
                result.add((String) o);
            }
            field.set(beanObject, result);

        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }


}
