package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;

public class DirectionsData {
    private String directId;
    private String directName;

    public DirectionsData(JSONObject updateDirections) {
        setDirectId((String) updateDirections.get("directId"));
        setDirectName((String) updateDirections.get("directName"));
    }

    public String getDirectId() {
        return directId;
    }

    private void setDirectId(String directId) {
        Util.requireNonNullOrEmpty(directId, "directId");
        this.directId = directId;
    }

    public String getDirectName() {
        return directName;
    }

    private void setDirectName(String directName) {
        Util.requireNonNull(directName, "directName");
        this.directName = directName;
    }

    @Override
    public String toString() {
        return "DirectionsData{" +
                "directId='" + directId + '\'' +
                ", directName='" + directName + '\'' +
                '}';
    }

}
