package ru.logist.sbat.jsonParser.jsonReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.logist.sbat.GlobalUtils;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Util {



    public static void checkFieldAvailability(String fieldName, JSONObject jsonObject) throws ValidatorException {
        if (!jsonObject.containsKey(fieldName))
            throw new ValidatorException(GlobalUtils.getParameterizedString("can't find field {} in {}", fieldName, jsonObject));
    }

    public static void checkCorrectType(Object fieldContent, Class fieldContentClazz, JSONObject jsonObject) throws ValidatorException {
        if (!fieldContent.getClass().equals(fieldContentClazz))
            throw new ValidatorException(GlobalUtils.getParameterizedString("fieldContent {} is not type of {} in {} ", fieldContent, fieldContentClazz.getSimpleName(), jsonObject));
    }

    public static void checkCorrectType(Object fieldContent, Class fieldContentClazz) throws ValidatorException {
        if (!fieldContent.getClass().equals(fieldContentClazz))
            throw new ValidatorException(GlobalUtils.getParameterizedString("fieldContent {} is not type of {}", fieldContent, fieldContentClazz.getSimpleName()));
    }

    public static void checkFieldAvailableAndNotNull(String fieldName, JSONObject jsonObject) throws ValidatorException {
        checkFieldAvailability(fieldName, jsonObject);
        if (jsonObject.get(fieldName) == null)
            throw new ValidatorException(GlobalUtils.getParameterizedString("field {} is null in {}", fieldName, jsonObject));
    }

    public static void checkFieldAvailableAndNotNullAndNotEmpty(String fieldName, JSONObject jsonObject) throws ValidatorException {
        checkFieldAvailableAndNotNull(fieldName, jsonObject);
        if (jsonObject.get(fieldName).equals(""))
            throw new ValidatorException((GlobalUtils.getParameterizedString("field {} is empty in {}", fieldName, jsonObject)));
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
                    throw new ValidatorException(GlobalUtils.getParameterizedString("value of {} must be greater or equal to '0' in {} ", fieldName, jsonObject));
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
                    Date date = getDate(formatter, (String) dateAsString);
                    field.set(beanObject, date);
                } catch (DateTimeParseException e) {
                    throw new ValidatorException(GlobalUtils.getParameterizedString("illegal date format {} in {}", dateAsString, jsonObject), e);
                }
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static void setNullIfEmptyOrSetValueDateTime(String fieldName, JSONObject jsonObject, Object beanObject, String beanObjectFieldName, DateTimeFormatter formatter) throws ValidatorException {
        try {
            Field field = beanObject.getClass().getDeclaredField(beanObjectFieldName);
            field.setAccessible(true);
            Object dateAsString = jsonObject.get(fieldName);
            if (dateAsString.equals(""))
                field.set(beanObject, null);
            else {
                Util.checkCorrectType(dateAsString, String.class, jsonObject);
                try {
                    Timestamp dateTime = getDateTime(formatter, (String) dateAsString);
                    field.set(beanObject, dateTime);
                } catch (DateTimeParseException e) {
                    throw new ValidatorException(GlobalUtils.getParameterizedString("illegal date format {} in {}", dateAsString, jsonObject), e);
                }
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static Timestamp getDateTime(DateTimeFormatter formatter, String dateAsString) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateAsString, formatter);
        java.util.Date out = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return new Timestamp(out.getTime());
    }

    public static Date getDate(DateTimeFormatter formatter, String dateAsString) {
        LocalDate localDate = LocalDate.parse(dateAsString, formatter);
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return new Date(Date.from(instant).getTime());
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
                throw new ValidatorException(GlobalUtils.getParameterizedString("status {} is not contained in set of possible statuses in {}", jsonValue, jsonObject));
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
                    throw new ValidatorException(GlobalUtils.getParameterizedString("illegal invoiceId {} in {}", o, jsonObject));
                }
                result.add((String) o);
            }
            field.set(beanObject, result);

        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }


}
