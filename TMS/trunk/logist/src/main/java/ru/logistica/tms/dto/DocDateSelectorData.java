package ru.logistica.tms.dto;

import ru.logistica.tms.util.UtcSimpleDateFormat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocDateSelectorData {
    public static final SimpleDateFormat dateFormat = new UtcSimpleDateFormat("yyyy-MM-dd");
    public Date utcDate;
    public Integer warehouseId;
    public Integer docId;

    public DocDateSelectorData(String docDateJsonString) throws ValidateDataException {
        try (JsonReader receivedDataReader = Json.createReader(new StringReader(docDateJsonString))){
            JsonObject receivedJsonObject = receivedDataReader.readObject();
            this.utcDate = dateFormat.parse(receivedJsonObject.getString("date"));
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
