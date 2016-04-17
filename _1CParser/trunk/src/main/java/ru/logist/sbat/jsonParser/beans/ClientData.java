package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;
import ru.logist.sbat.jsonParser.ValidatorException;

public class ClientData {
    public static final String FN_CLIENT_ID = "clientId";
    public static final String FN_CLIENT_NAME = "clientName";
    public static final String FN_CLIENT_INN = "clientINN";
    public static final String FN_CLIENT_PASSWORD = "clientPassword";

    @Unique
    private String clientId;
    private String clientName;
    private String clientINN;
    private String clientPassword;

    public ClientData(JSONObject updateClient) throws ValidatorException {
        // check fields
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_CLIENT_ID, updateClient);
        Util.checkFieldAvailableAndNotNull           (FN_CLIENT_ID, updateClient);
        Util.checkFieldAvailableAndNotNull           (FN_CLIENT_NAME, updateClient);
        Util.checkFieldAvailableAndNotNull           (FN_CLIENT_INN, updateClient);
        Util.checkFieldAvailableAndNotNull           (FN_CLIENT_PASSWORD, updateClient);

        // set values
        Util.setStringValue(FN_CLIENT_ID, updateClient, this, "clientId");
        Util.setStringValue(FN_CLIENT_NAME, updateClient, this, "clientName");
        Util.setStringValue(FN_CLIENT_INN, updateClient, this, "clientINN");
        Util.setStringValue(FN_CLIENT_PASSWORD, updateClient, this, "clientPassword");
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

    public String getClientPassword() {
        return clientPassword;
    }

    public boolean hasValidPassword() {
        return (clientPassword!= null && !clientPassword.isEmpty());
    }

    @Override
    public String toString() {
        return "ClientData{" +
                "clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientINN='" + clientINN + '\'' +
                ", clientPassword='" + clientPassword + '\'' +
                '}';
    }
}
