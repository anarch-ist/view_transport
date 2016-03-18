package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;
import ru.logist.sbat.jsonParser.ValidatorException;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StatusData {
    // field names in JSON file
    private static final String FN_REQUEST_ID = "requestId";
    private static final String FN_NUM_BOXES = "num_boxes";
    private static final String FN_STATUS = "status";
    private static final String FN_TIME_OUT_STATUS = "timeOutStatus";
    private static final String FN_COMMENT = "Comment";
    private static final Set<String> possibleStatuses = new HashSet<>(Arrays.asList(
            "UNKNOWN", "SAVED", "APPROVING", "RESERVED", "APPROVED", "STOP_LIST",
            "CREDIT_LIMIT", "RASH_CREATED", "COLLECTING", "CHECK", "CHECK_PASSED", "ADJUSTMENTS_MADE",
            "PACKAGING", "CHECK_BOXES", "READY", "TRANSPORTATION"
    ));
    private static final DateTimeFormatter timeOutStatusFormatter = DateTimeFormatter.ofPattern("uu.MM.dd,HH:mm:ss");

    private String requestId;
    private Long numBoxes;
    private String status;
    private Date timeOutStatus;
    private String comment;

    public StatusData(JSONObject updateStatus) {
        // check fields
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_REQUEST_ID, updateStatus);
        Util.checkFieldAvailableAndNotNull           (FN_NUM_BOXES, updateStatus);
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_STATUS, updateStatus);
        Util.checkFieldAvailableAndNotNull           (FN_TIME_OUT_STATUS, updateStatus);
        Util.checkFieldAvailableAndNotNull           (FN_COMMENT, updateStatus);

        //set values
        Util.setStringValue              (FN_REQUEST_ID, updateStatus, this, "requestId");
        Util.setNullIfEmptyOrSetValueLong(FN_NUM_BOXES, updateStatus, this, "numBoxes");
        Util.setStringValue              (FN_STATUS, updateStatus, this, "status", possibleStatuses);
        Util.setNullIfEmptyOrSetValueDate(FN_TIME_OUT_STATUS, updateStatus, this, "timeOutStatus", timeOutStatusFormatter);
        Util.setStringValue              (FN_COMMENT, updateStatus, this, "comment");
    }

    public String getRequestId() {
        return requestId;
    }

    public Long getNumBoxes() {
        return numBoxes;
    }

    public String getStatus() {
        return status;
    }

    public Date getTimeOutStatus() {
        return timeOutStatus;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "StatusData{" +
                "requestId='" + requestId + '\'' +
                ", numBoxes=" + numBoxes +
                ", status='" + status + '\'' +
                ", timeOutStatus=" + timeOutStatus +
                ", comment='" + comment + '\'' +
                '}';
    }
}
