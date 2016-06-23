package ru.logistica.tms.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Objects;

public class CriptUtils {

    private CriptUtils() {}

    public static String generateSalt() {
        return RandomStringUtils.randomAlphanumeric(16);
    }

    public static String generatePassAndSalt(String pass, String salt) {
        Objects.requireNonNull(pass);
        Objects.requireNonNull(salt);
        if (salt.length() != 16)
            throw new IllegalArgumentException("salt length must be 16");
        return CriptUtils.md5(CriptUtils.md5(pass) + salt);
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

}
