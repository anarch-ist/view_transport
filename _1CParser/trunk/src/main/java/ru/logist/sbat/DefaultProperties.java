package ru.logist.sbat;

import java.util.Properties;

public class DefaultProperties extends Properties {
    public DefaultProperties() {
        this.put("mode", "generate");
        this.put("generatePeriod", 10); // seconds
        this.put("user", "root");
        this.put("password", "rtbyg7895otlgorit");
        this.put("url", "jdbc:mysql://localhost:3306/project_database");
    }
}
