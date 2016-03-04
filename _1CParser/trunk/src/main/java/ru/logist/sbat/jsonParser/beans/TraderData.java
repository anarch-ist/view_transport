package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;

public class TraderData {
    private String traderId;
    private String traderName;
    private String traderEmails;
    private String traderPhone;

    public TraderData(JSONObject updateTrader) {
        setTraderId((String) updateTrader.get("traderId"));
        setTraderName((String) updateTrader.get("traderName"));
        setTraderEmails((String) updateTrader.get("traderEMail"));
        setTraderPhone((String) updateTrader.get("traderPhone"));
    }

    public String getTraderId() {
        return traderId;
    }

    private void setTraderId(String traderId) {
        Util.requireNonNullOrEmpty(traderId, "traderId");
        this.traderId = traderId;
    }

    public String getTraderName() {
        return traderName;
    }

    private void setTraderName(String traderName) {
        Util.requireNonNull(traderName, "traderName");
        this.traderName = traderName;
    }

    public String getTraderEmails() {
        return traderEmails;
    }

    private void setTraderEmails(String traderEmails) {
        Util.requireNonNull(traderEmails, "traderEMail");
        this.traderEmails = traderEmails;
    }

    public String getTraderPhone() {
        return traderPhone;
    }

    private void setTraderPhone(String traderPhone) {
        Util.requireNonNull(traderPhone, "traderPhone");
        this.traderPhone = traderPhone;
    }

    @Override
    public String toString() {
        return "TraderData{" +
                "traderId='" + traderId + '\'' +
                ", traderName='" + traderName + '\'' +
                ", traderEmails='" + traderEmails + '\'' +
                ", traderPhone='" + traderPhone + '\'' +
                '}';
    }
}
