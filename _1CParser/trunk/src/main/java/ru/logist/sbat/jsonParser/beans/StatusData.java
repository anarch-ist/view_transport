package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StatusData {
    private static final Set<String> possibleStatuses = new HashSet<>(Arrays.asList(
            "SAVED", "APPROVING", "RESERVED", "APPROVED", "STOP_LIST",
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
        setRequestId((String) updateStatus.get("requestId"));
        setNumBoxes((Long) updateStatus.get("num_boxes"));
        setStatus((String) updateStatus.get("status"));
        setTimeOutStatus((String) updateStatus.get("timeOutStatus"));
        setComment((String) updateStatus.get("comment"));
    }

    public String getRequestId() {
        return requestId;
    }

    private void setRequestId(String requestId) {
        Util.requireNonNullOrEmpty(requestId, "requestId");
        this.requestId = requestId;
    }

    public Long getNumBoxes() {
        return numBoxes;
    }

    // TODO fix it
    private void setNumBoxes(Long numBoxes) {
    //    Util.requireNonNull(numBoxes, "num_boxes");
        this.numBoxes = numBoxes;
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        Util.withValidatorExceptionRedirect(() -> {
            if (!possibleStatuses.contains(status))
                throw new IllegalArgumentException("status [" + status + "] is not contained in set of possible statuses [" + possibleStatuses + "]");
            this.status = status;
        });
    }

    public Date getTimeOutStatus() {
        return timeOutStatus;
    }

    // TODO fix it
    private void setTimeOutStatus(String timeOutStatusAsString) {
        //timeOutStatus = Util.getDateWithCheck(timeOutStatusAsString, "timeOutStatus", timeOutStatusFormatter);
    }

    public String getComment() {
        return comment;
    }

    // TODO fix it make warning
    private void setComment(String comment) {
        //Util.requireNonNull(comment, "comment");
        this.comment = comment;
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
