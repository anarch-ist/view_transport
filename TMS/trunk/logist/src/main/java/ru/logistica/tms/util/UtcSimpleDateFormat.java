package ru.logistica.tms.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class UtcSimpleDateFormat extends SimpleDateFormat {
    public UtcSimpleDateFormat(String pattern) {
        super(pattern);
        this.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
}
