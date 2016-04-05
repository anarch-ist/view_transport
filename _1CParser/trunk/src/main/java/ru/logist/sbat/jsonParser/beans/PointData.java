package ru.logist.sbat.jsonParser.beans;


import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;
import ru.logist.sbat.jsonParser.ValidatorException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PointData {

    private static final String FN_POINT_ID = "pointId";
    private static final String FN_POINT_NAME = "pointName";
    private static final String FN_POINT_ADDRESS = "pointAdress";
    private static final String FN_POINT_TYPE = "pointType";
    private static final String FN_POINT_EMAIL = "pointEmail";
    private static final String FN_RESPONSIBLE_PERSON_ID = "responsiblePersonId";
    public static final Set<String> POINT_TYPES = new HashSet<>(Arrays.asList("WAREHOUSE", "AGENCY"));

    @Unique
    private String pointId; //CONSTRAINT not null or empty
    private String pointName; //CONSTRAINT not null
    private String pointAddress; //CONSTRAINT not null or empty
    private String pointType; //CONSTRAINT one of two values (WAREHOUSE, AGENCY)
    private String pointEmails; //CONSTRAINT not null
    private String responsiblePersonId; //CONSTRAINT not null

    public PointData(JSONObject updatePoint) {
        // check fields
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_POINT_ID, updatePoint);
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_POINT_NAME, updatePoint);
        Util.checkFieldAvailableAndNotNull           (FN_POINT_ADDRESS, updatePoint);
        Util.checkFieldAvailableAndNotNull           (FN_POINT_TYPE, updatePoint);
        Util.checkFieldAvailableAndNotNull           (FN_POINT_EMAIL, updatePoint);
        Util.checkFieldAvailableAndNotNull           (FN_RESPONSIBLE_PERSON_ID, updatePoint);

        // set values
        Util.setStringValue(FN_POINT_ID, updatePoint, this, "pointId");
        Util.setStringValue(FN_POINT_NAME, updatePoint, this, "pointName");
        Util.setStringValue(FN_POINT_ADDRESS, updatePoint, this, "pointAddress");
        Util.setStringValue(FN_POINT_TYPE, updatePoint, this, "pointType", POINT_TYPES);
        Util.setStringValue(FN_POINT_EMAIL, updatePoint, this, "pointEmails");
        Util.setStringValue(FN_RESPONSIBLE_PERSON_ID, updatePoint, this, "responsiblePersonId");
    }

    public String getPointId() {
        return pointId;
    }

    public String getResponsiblePersonId() {
        return responsiblePersonId;
    }

    public String getPointName() {
        return pointName;
    }

    public String getPointAddress() {
        return pointAddress;
    }

    public String getPointType() {
        return pointType;
    }

    public String getPointEmails() {
        return pointEmails;
    }

    @Override
    public String toString() {
        return "PointData{" +
                "pointId='" + pointId + '\'' +
                ", responsiblePersonId='" + responsiblePersonId + '\'' +
                ", pointName='" + pointName + '\'' +
                ", pointAddress='" + pointAddress + '\'' +
                ", pointType='" + pointType + '\'' +
                ", pointEmails='" + pointEmails + '\'' +
                '}';
    }
}
