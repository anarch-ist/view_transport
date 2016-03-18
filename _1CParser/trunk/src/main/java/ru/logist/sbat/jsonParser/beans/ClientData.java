package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;

public class ClientData {
    private static final String FN_CLIENT_ID = "clientId";
    private static final String FN_CLIENT_NAME = "clientName";
    private static final String FN_CLIENT_INN = "clientINN";

    private String clientId;
    private String clientName;
    private String clientINN;

    public ClientData(JSONObject updateClient) {
        // check fields
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_CLIENT_ID, updateClient);
        Util.checkFieldAvailableAndNotNull           (FN_CLIENT_NAME, updateClient);
        Util.checkFieldAvailableAndNotNull           (FN_CLIENT_INN, updateClient);

        // set values
        Util.setStringValue(FN_CLIENT_ID, updateClient, this, "clientId");
        Util.setStringValue(FN_CLIENT_NAME, updateClient, this, "clientName");
        Util.setStringValue(FN_CLIENT_INN, updateClient, this, "clientINN");
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientINN() {
        return clientINN;
    }

    @Override
    public String toString() {
        return "ClientData{" +
                "clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientINN='" + clientINN + '\'' +
                '}';
    }
}
