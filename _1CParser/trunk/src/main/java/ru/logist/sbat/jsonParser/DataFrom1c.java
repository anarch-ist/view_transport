package ru.logist.sbat.jsonParser;

import org.json.simple.JSONObject;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DataFrom1c {
    private static final DateTimeFormatter dateTimeSQLFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");

    private String server; // CONSTRAINT not empty string not null
    private Integer packageNumber; //CONSTRAINT >=0 not null
    private Date created; // CONSTRAINT not null
    private PackageData packageData; // CONSTRAINT not null

    public DataFrom1c(JSONObject dataFrom1cAsJsonObject) {
        setServer((String)dataFrom1cAsJsonObject.get("server"));
        setCreated((String)dataFrom1cAsJsonObject.get("created"));
        setPackageNumber((String)dataFrom1cAsJsonObject.get("packageNumber"));
        setPackageData((JSONObject) dataFrom1cAsJsonObject.get("packageData"));
    }

    public String getServer() {
        return server;
    }

    private void setServer(String server) {
        Run.requireNonNullOrEmpty(server, "server");
        this.server = server;
    }

    public Integer getPackageNumber() {
        return packageNumber;
    }

    private void setPackageNumber(String packageNumberAsString) {
        Run.withValidatorExceptionRedirect(() -> {
            Objects.requireNonNull(packageNumberAsString, "[package number] must not be null");
            packageNumber = Integer.parseInt(packageNumberAsString);
            if (packageNumber < 0)
                throw new IllegalArgumentException("[package number] must be greater or equal 0");
        });
    }

    public Date getCreated() {
        return created;
    }

    private void setCreated(String createdDateAsString) {
        Run.requireNonNullOrEmpty(createdDateAsString, "createdDate");
        Run.withValidatorExceptionRedirect(() -> created = Date.valueOf(LocalDate.parse(createdDateAsString, dateTimeSQLFormatter)));
    }

    public PackageData getPackageData() {
        return packageData;
    }

    private void setPackageData(JSONObject packageDataAsJsonObject) {
        Run.withValidatorExceptionRedirect(() -> {
            Objects.requireNonNull(packageDataAsJsonObject, "packageData must not be null");
            this.packageData = new PackageData(packageDataAsJsonObject);
        });
    }

    @Override
    public String toString() {
        return "DataFrom1c{" +
                "server='" + server + '\'' +
                ", packageNumber=" + packageNumber +
                ", created=" + created +
                ", packageData=" + packageData +
                '}';
    }
}
