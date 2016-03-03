package ru.logist.sbat.jsonParser;


import org.json.simple.JSONObject;

import java.util.Arrays;

public class PointData {
    private static final String[] POINT_TYPES = {"WAREHOUSE", "AGENCY"};

    private String pointId; //CONSTRAINT not null or empty
    private String responsiblePersonId; //CONSTRAINT not null
    private String pointName; //CONSTRAINT not null
    private String pointAddress; //CONSTRAINT not null or empty
    private String pointType; //CONSTRAINT one of two values (WAREHOUSE, AGENCY)
    private String pointEmails; //CONSTRAINT not null

    public PointData(JSONObject updatePoint) {
        setPointId((String)updatePoint.get("pointId"));
        setResponsiblePersonId((String)updatePoint.get("responsiblePersonId"));
        setPointName((String)updatePoint.get("pointName"));
        setPointAddress((String)updatePoint.get("pointAdress"));
        setPointType((String)updatePoint.get("pointType"));
        setPointEmails((String)updatePoint.get("pointEmail"));
    }

    public String getPointId() {
        return pointId;
    }

    private void setPointId(String pointId) {
        Run.requireNonNullOrEmpty(pointId, "pointId");
        this.pointId = pointId;
    }

    public String getResponsiblePersonId() {
        return responsiblePersonId;
    }

    private void setResponsiblePersonId(String responsiblePersonId) {
        Run.requireNonNull(responsiblePersonId, "responsiblePersonId");
        this.responsiblePersonId = responsiblePersonId;
    }

    public String getPointName() {
        return pointName;
    }

    private void setPointName(String pointName) {
        Run.requireNonNull(pointName, "responsiblePersonId");
        this.pointName = pointName;
    }

    public String getPointAddress() {
        return pointAddress;
    }

    private void setPointAddress(String pointAddress) {
        Run.requireNonNull(pointAddress, "pointAddress");
        this.pointAddress = pointAddress;
    }

    public String getPointType() {
        return pointType;
    }

    private void setPointType(String pointType) {
        boolean isElement = false;
        for (String pointTypesElement: POINT_TYPES) {
            if (pointType.equals(pointTypesElement)){
                isElement = true;
                break;
            }
        }
        if (!isElement)
            throw new ValidatorException("point type must be one of values: "+ Arrays.toString(POINT_TYPES));
        this.pointType = pointType;
    }

    public String getPointEmails() {
        return pointEmails;
    }

    private void setPointEmails(String pointEmails) {
        Run.requireNonNull(pointEmails, "pointEmails");
        this.pointEmails = pointEmails;
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
