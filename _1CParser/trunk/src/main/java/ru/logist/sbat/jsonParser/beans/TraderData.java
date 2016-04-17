package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;
import ru.logist.sbat.jsonParser.ValidatorException;

public class TraderData {
    public static final String FN_TRADER_ID = "traderId";
    public static final String FN_TRADER_NAME = "traderName";
    public static final String FN_TRADER_EMAIL = "traderEMail";
    public static final String FN_TRADER_PHONE = "traderPhone";
    public static final String FN_TRADER_OFFICE = "traderOffice";
    public static final String FN_TRADER_LOGIN = "traderLogin";
    public static final String FN_TRADER_PASSWORD = "traderPassword";

    @Unique
    private String traderId;
    private String traderName;
    private String traderEmails;
    private String traderPhone;
    private String traderOffice;


    @Unique
    private String traderLogin;
    private String traderPassword;

    public TraderData(JSONObject updateTrader) throws ValidatorException {

        // check fields
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_TRADER_ID, updateTrader);
        Util.checkFieldAvailableAndNotNull           (FN_TRADER_NAME, updateTrader);
        Util.checkFieldAvailableAndNotNull           (FN_TRADER_EMAIL, updateTrader);
        Util.checkFieldAvailableAndNotNull           (FN_TRADER_PHONE, updateTrader);
        Util.checkFieldAvailableAndNotNull           (FN_TRADER_OFFICE, updateTrader);
        Util.checkFieldAvailableAndNotNull           (FN_TRADER_LOGIN, updateTrader);
        Util.checkFieldAvailableAndNotNull           (FN_TRADER_PASSWORD, updateTrader);

        //set values
        Util.setStringValue(FN_TRADER_ID, updateTrader, this, "traderId");
        Util.setStringValue(FN_TRADER_NAME, updateTrader, this, "traderName");
        Util.setStringValue(FN_TRADER_EMAIL, updateTrader, this, "traderEmails");
        Util.setStringValue(FN_TRADER_PHONE, updateTrader, this, "traderPhone");
        Util.setStringValue(FN_TRADER_OFFICE, updateTrader, this, "traderOffice");
        Util.setStringValue(FN_TRADER_LOGIN, updateTrader, this, "traderLogin");
        Util.setStringValue(FN_TRADER_PASSWORD, updateTrader, this, "traderPassword");
    }

    public String getTraderId() {
        return traderId;
    }

    public String getTraderName() {
        return traderName;
    }

    public String getTraderEmails() {
        return traderEmails;
    }

    public String getTraderPhone() {
        return traderPhone;
    }

    public String getTraderOffice() {
        return traderOffice;
    }

    public String getTraderLogin() {
        return traderLogin;
    }

    public String getTraderPassword() {
        return traderPassword;
    }

    public boolean hasValidLoginAndPassword() {
        return traderLogin != null && !traderLogin.isEmpty() && traderPassword != null && !traderPassword.isEmpty();
    }

    @Override
    public String toString() {
        return "TraderData{" +
                "traderId='" + traderId + '\'' +
                ", traderName='" + traderName + '\'' +
                ", traderEmails='" + traderEmails + '\'' +
                ", traderPhone='" + traderPhone + '\'' +
                ", traderOffice='" + traderOffice + '\'' +
                ", traderLogin='" + traderLogin + '\'' +
                ", traderPassword='" + traderPassword + '\'' +
                '}';
    }
}
