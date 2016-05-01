package ru.logist.sbat.resourcesInit;

import ru.logist.sbat.jsonParser.jsonReader.Util;

import java.util.Objects;
import java.util.Properties;

public class PropertiesPojo {
    private Properties properties;
    private String url;
    private String user;
    private String password;
    private String dbName;
    private String encoding;
    private String jsonDataDir;
    private String backupDir;
    private String responseDir;
    private String logsDir;

    public PropertiesPojo(Properties properties) {
        Objects.requireNonNull(properties, "property must not be null");
        this.properties = properties;

        this.url = getValueWithNotNullCheck("url");
        this.user = getValueWithNotNullCheck("user");
        this.password = getValueWithNotNullCheck("password");
        this.dbName = getValueWithNotNullCheck("dbName");
        this.encoding = getValueWithNotNullCheck("encoding");
        this.jsonDataDir = getValueWithNotNullCheck("jsonDataDir");
        this.backupDir = getValueWithNotNullCheck("backupDir");
        this.responseDir = getValueWithNotNullCheck("responseDir");
        this.logsDir = getValueWithNotNullCheck("logsDir");
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDbName() {
        return dbName;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getJsonDataDir() {
        return jsonDataDir;
    }

    public String getBackupDir() {
        return backupDir;
    }

    public String getResponseDir() {
        return responseDir;
    }

    public String getLogsDir() {
        return logsDir;
    }

    private String getValueWithNotNullCheck(String propertyName) {
        String property = this.properties.getProperty(propertyName);
        if (property == null) {
            throw new IllegalArgumentException(Util.getParameterizedString("can't find property {}", propertyName));
        }
        return property;
    }

    @Override
    public String toString() {
        return "DATABASE CONNECTION PARAMS:\n" +
                Util.getParameterizedString("url = {}\n", url) +
                Util.getParameterizedString("user = {}\n", user) +
                Util.getParameterizedString("password = {}\n", password) +
                Util.getParameterizedString("dbName = {}\n", dbName) +
                Util.getParameterizedString("encoding = {}\n", encoding) +
                "DIRECTORY PARAMS:\n" +
                Util.getParameterizedString("json data directory = {}\n", jsonDataDir) +
                Util.getParameterizedString("backup directory = {}\n", backupDir) +
                Util.getParameterizedString("response directory = {}\n", responseDir) +
                Util.getParameterizedString("logs directory = {}", logsDir);
    }
}
