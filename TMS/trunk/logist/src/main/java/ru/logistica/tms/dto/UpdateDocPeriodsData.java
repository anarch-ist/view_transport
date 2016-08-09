package ru.logistica.tms.dto;

import ru.logistica.tms.util.JsonUtils;

import javax.json.JsonObject;

public class UpdateDocPeriodsData {

    public final long docPeriodsId;
    public final boolean isBegin;

    public UpdateDocPeriodsData(long docPeriodsId, boolean isBegin) {
        this.docPeriodsId = docPeriodsId;
        this.isBegin = isBegin;
    }

    public UpdateDocPeriodsData(String updateDocPeriodsJsonString) throws ValidateDataException {
        try {
            JsonObject receivedJsonObject = JsonUtils.parseStringAsObject(updateDocPeriodsJsonString);
            this.docPeriodsId = receivedJsonObject.getJsonNumber("docPeriodsId").longValueExact();
            this.isBegin = receivedJsonObject.getBoolean("isBegin");
        } catch (Exception e) {
            throw new ValidateDataException(e);
        }
    }

    public long getDocPeriodsId() {
        return docPeriodsId;
    }

    public boolean getIsBegin() {
        return isBegin;
    }

    @Override
    public String toString() {
        return "UpdateDocPeriodsData{" +
                "docPeriodsId=" + docPeriodsId +
                ", isBegin=" + isBegin +
                '}';
    }

}
