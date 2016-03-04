package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;

public class ClientData {
    private String clientId;
    private String clientName;
    private String clientINN;

    public ClientData(JSONObject updateClient) {
        setClientId((String) updateClient.get("clientId"));
        setClientName((String) updateClient.get("clientName"));
        setClientINN((String) updateClient.get("clientINN"));
    }

    public String getClientId() {
        return clientId;
    }

    private void setClientId(String clientId) {
        Util.requireNonNullOrEmpty(clientId, "clientId");
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    private void setClientName(String clientName) {
        Util.requireNonNull(clientName, "clientName");
        this.clientName = clientName;
    }

    public String getClientINN() {
        return clientINN;
    }

    private void setClientINN(String clientINN) {
        Util.requireNonNull(clientINN, "clientINN");
        this.clientINN = clientINN;
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
