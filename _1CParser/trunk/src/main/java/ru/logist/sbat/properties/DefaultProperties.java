package ru.logist.sbat.properties;

import java.util.Properties;

public class DefaultProperties extends Properties {
    public DefaultProperties() {
        // property can be parse or generate.
        this.put("mode", "parse");
        this.put("generatePeriod", "10"); // seconds
        this.put("user", "root");
        this.put("password", "rtbyg7895otlgorit");
        this.put("url", "jdbc:mysql://localhost:3306/");
        this.put("dbName", "transmaster_transport_db");
        this.put("encoding", "useUnicode=true&characterEncoding=UTF-8");
    }
}
