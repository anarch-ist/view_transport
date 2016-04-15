package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;
import ru.logist.sbat.jsonParser.ValidatorException;

public class DirectionsData {
    private static final String FN_DIRECT_ID = "directId";
    private static final String FN_DIRECT_NAME = "directName";

    @Unique
    private String directId;
    private String directName;

    public DirectionsData(JSONObject updateDirections) throws ValidatorException {
        // check fields
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_DIRECT_ID, updateDirections);
        Util.checkFieldAvailableAndNotNull           (FN_DIRECT_NAME, updateDirections);

        // set values
        Util.setStringValue(FN_DIRECT_ID, updateDirections, this, "directId");
        Util.setStringValue(FN_DIRECT_NAME, updateDirections, this, "directName");
    }

    public String getDirectId() {
        return directId;
    }

    public String getDirectName() {
        return directName;
    }

    @Override
    public String toString() {
        return "DirectionsData{" +
                "directId='" + directId + '\'' +
                ", directName='" + directName + '\'' +
                '}';
    }

}
