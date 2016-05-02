package ru.logist.sbat.resourcesInit;

import ru.logist.sbat.GlobalUtils;

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
            throw new IllegalArgumentException(GlobalUtils.getParameterizedString("can't find property {}", propertyName));
        }
        return property;
    }

    @Override
    public String toString() {
        return "DATABASE CONNECTION PARAMS:\n" +
                GlobalUtils.getParameterizedString("url = {}\n", url) +
                GlobalUtils.getParameterizedString("user = {}\n", user) +
                GlobalUtils.getParameterizedString("password = {}\n", password) +
                GlobalUtils.getParameterizedString("dbName = {}\n", dbName) +
                GlobalUtils.getParameterizedString("encoding = {}\n", encoding) +
                "DIRECTORY PARAMS:\n" +
                GlobalUtils.getParameterizedString("json data directory = {}\n", jsonDataDir) +
                GlobalUtils.getParameterizedString("backup directory = {}\n", backupDir) +
                GlobalUtils.getParameterizedString("response directory = {}\n", responseDir) +
                GlobalUtils.getParameterizedString("logs directory = {}", logsDir);
    }
}
