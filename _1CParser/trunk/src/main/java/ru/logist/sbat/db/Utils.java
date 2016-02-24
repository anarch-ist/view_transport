package ru.logist.sbat.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class Utils {
    private static final Logger logger = LogManager.getLogger(InsertOrUpdateTransactionScript.class);

    private Utils() {}

    //TODO check this algo the same as php use
    // 32 digits like in php
    public static String md5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {/*NOPE*/}
        return null;
    }

    public static UniqueCheck getUniqueCheckObject(String parameterName) {
        return new UniqueCheck(parameterName);
    }

    public static UniqueCheck getUniqueCheckObject(String parameterName, Collection<String> initialStrings) {
        return new UniqueCheck(parameterName, initialStrings);
    }

    public static class UniqueCheck {
        String parameterName;
        private Set<String> uniqueStringSet = new HashSet<>();

        public UniqueCheck(String parameterName, Collection<String> initialStrings) {
            this(parameterName);
            this.uniqueStringSet.addAll(initialStrings);
        }

        public UniqueCheck(String parameterName) {
            this.parameterName = parameterName;
        }

        boolean isUnique(String string) {
            if (!uniqueStringSet.add(string)) {
                logger.warn("{} = [{}] was duplicated", parameterName, string);
                return false;
            } else
                return true;
        }
    }

    /**
     * if allRouteNames contains routeNameCandidate then direction name changed
     * @param allRouteNames
     * @param duplicatedRouteName
     */
    public static String getUniqueDirectionName(final Set<String> allRouteNames, final String duplicatedRouteName) {
        int count = 0;
        do {
            count++;
        } while (allRouteNames.contains(duplicatedRouteName + count + ""));
        String generatedDirectionName = duplicatedRouteName + count + "";
        logger.warn("direction name [{}] was duplicated, generated direction name = [{}]", duplicatedRouteName, generatedDirectionName);
        return generatedDirectionName;
    }

    public static java.sql.Date getSqlDateFromString(String dateString) {
        if (dateString == null)
            return null;
        LocalDate requestDate = LocalDate.parse(dateString, DateTimeFormatter.BASIC_ISO_DATE);
        Date date = Date.valueOf(requestDate);
        return new java.sql.Date(date.getTime());
    }

}
