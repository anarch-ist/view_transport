package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DataFrom1c {
    private static final DateTimeFormatter dateTimeSQLFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");

    private String server; // CONSTRAINT not empty string not null
    private Long packageNumber; //CONSTRAINT >=0 not null
    private Date created; // CONSTRAINT not null
    private PackageData packageData; // CONSTRAINT not null

    public DataFrom1c(JSONObject dataFrom1cAsJsonObject) {
        setServer((String)dataFrom1cAsJsonObject.get("server"));
        setCreated((String)dataFrom1cAsJsonObject.get("created"));
        setPackageNumber((Long)dataFrom1cAsJsonObject.get("packageNumber"));
        setPackageData((JSONObject) dataFrom1cAsJsonObject.get("packageData"));
    }

    public String getServer() {
        return server;
    }

    private void setServer(String server) {
        Util.requireNonNullOrEmpty(server, "server");
        this.server = server;
    }

    public Long getPackageNumber() {
        return packageNumber;
    }

    private void setPackageNumber(Long packageNumber) {
        Util.requireNonNull(packageNumber, "packageNumber");
        this.packageNumber = packageNumber;
    }

    public Date getCreated() {
        return created;
    }

    private void setCreated(String createdDateAsString) {
        Util.requireNonNullOrEmpty(createdDateAsString, "createdDate");
        Util.withValidatorExceptionRedirect(() -> created = Date.valueOf(LocalDate.parse(createdDateAsString, dateTimeSQLFormatter)));
    }

    public PackageData getPackageData() {
        return packageData;
    }

    private void setPackageData(JSONObject packageDataAsJsonObject) {
        Util.withValidatorExceptionRedirect(() -> {
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
