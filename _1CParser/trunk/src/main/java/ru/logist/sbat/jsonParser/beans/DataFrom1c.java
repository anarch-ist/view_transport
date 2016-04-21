package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;
import ru.logist.sbat.jsonParser.ValidatorException;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class DataFrom1c {
    public static final String FN_DATA_FROM_1C = "dataFrom1C";
    public static final String FN_SERVER = "server";
    public static final String FN_PACKAGE_NUMBER = "packageNumber";
    public static final String FN_CREATED = "created";
    public static final String FN_PACKAGE_DATA = "packageData";

    private static final DateTimeFormatter dateTimeSQLFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");

    private String server;
    private Long packageNumber;
    private Timestamp created;
    private PackageData packageData;
    private String rawJsonObject;

    public DataFrom1c(JSONObject dataFrom1cAsJsonObject) throws ValidatorException {
        // check fields
        Util.checkFieldAvailableAndNotNull(FN_DATA_FROM_1C, dataFrom1cAsJsonObject);
        Util.checkCorrectType(dataFrom1cAsJsonObject.get(FN_DATA_FROM_1C), JSONObject.class, dataFrom1cAsJsonObject);
        JSONObject dataFrom1c = (JSONObject) dataFrom1cAsJsonObject.get(FN_DATA_FROM_1C);
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_SERVER, dataFrom1c);
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_PACKAGE_NUMBER, dataFrom1c);
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_CREATED, dataFrom1c);
        Util.checkFieldAvailableAndNotNull           (FN_PACKAGE_DATA, dataFrom1c);
        Util.checkCorrectType(dataFrom1c.get(FN_PACKAGE_DATA), JSONObject.class, dataFrom1c);

        //set values
        Util.setStringValue              (FN_SERVER, dataFrom1c, this, "server");
        Util.setNullIfEmptyOrSetValueLong(FN_PACKAGE_NUMBER, dataFrom1c, this, "packageNumber");
        Util.setNullIfEmptyOrSetValueDateTime(FN_CREATED, dataFrom1c, this, "created", dateTimeSQLFormatter);
        this.packageData = new PackageData((JSONObject) dataFrom1c.get(FN_PACKAGE_DATA));
        this.rawJsonObject = dataFrom1cAsJsonObject.toJSONString();
    }

    public String getRawJsonObject() {
        return rawJsonObject;
    }

    public String getServer() {
        return server;
    }

    public Long getPackageNumber() {
        return packageNumber;
    }

    public Timestamp getCreated() {
        return created;
    }

    public PackageData getPackageData() {
        return packageData;
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
