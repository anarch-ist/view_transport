package ru.logistica.tms.dto;

import ru.logistica.tms.util.JsonUtils;
import ru.logistica.tms.util.UtcSimpleDateFormat;

import javax.json.JsonObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocDateSelectorData {
    //public static final SimpleDateFormat dateFormat = new UtcSimpleDateFormat("yyyy-MM-dd");
    public final long utcDate;
    public final Integer warehouseId;
    public final Integer docId;

    public DocDateSelectorData(long utcDate, Integer warehouseId, Integer docId) {
        this.utcDate = utcDate;
        this.warehouseId = warehouseId;
        this.docId = docId;
    }

    public DocDateSelectorData(String docDateJsonString) throws ValidateDataException {
        try {
            JsonObject receivedJsonObject = JsonUtils.parseStringAsObject(docDateJsonString);
            this.utcDate = receivedJsonObject.getJsonNumber("date").longValueExact();
            this.warehouseId = receivedJsonObject.getInt("warehouseId");
            this.docId = receivedJsonObject.getInt("docId");
        } catch (Exception e) {
            throw new ValidateDataException(e);
        }
    }

    @Override
    public String toString() {
        return "DocDateSelectorData{" +
                "utcDate=" + utcDate +
                ", warehouseId=" + warehouseId +
                ", docId=" + docId +
                '}';
    }
}
