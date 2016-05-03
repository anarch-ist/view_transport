package ru.logist.sbat.db;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Set;

public class Utils {
    private static final Logger logger = LogManager.getLogger(DBManager.class);

    private Utils() {}

    public static String generateSalt() {
        return RandomStringUtils.randomAlphanumeric(16);
    }

    public static String generatePassAndSalt(String pass, String salt) {
        Objects.requireNonNull(pass);
        Objects.requireNonNull(salt);
        if (salt.length() != 16)
            throw new IllegalArgumentException("salt length must be 16");
        return Utils.md5(Utils.md5(pass) + salt);
    }

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

}
