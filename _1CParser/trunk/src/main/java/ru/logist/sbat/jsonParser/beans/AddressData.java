package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;

public class AddressData {
    private static final String FN_ADDRESS_ID = "addressId";
    private static final String FN_ADDRESS_SHORT = "addressShot";
    private static final String FN_ADDRESS_FULL = "addressFull";
    private static final String FN_DELIVERY_AREA_ID = "deliveryAreaId";

    private String addressId; //not null and not empty
    private String addressShot; // not null
    private String addressFull; // not null
    private String deliveryAreaId; // not null

    public AddressData(JSONObject updateAddress) {
        // check fields
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_ADDRESS_ID, updateAddress);
        Util.checkFieldAvailableAndNotNull           (FN_ADDRESS_SHORT, updateAddress);
        Util.checkFieldAvailableAndNotNull           (FN_ADDRESS_FULL, updateAddress);
        Util.checkFieldAvailableAndNotNull           (FN_DELIVERY_AREA_ID, updateAddress);

        // set values
        Util.setStringValue(FN_ADDRESS_ID, updateAddress, this, "addressId");
        Util.setStringValue(FN_ADDRESS_SHORT, updateAddress, this, "addressShot");
        Util.setStringValue(FN_ADDRESS_FULL, updateAddress, this, "addressFull");
        Util.setStringValue(FN_DELIVERY_AREA_ID, updateAddress, this, "deliveryAreaId");
    }

    public String getAddressId() {
        return addressId;
    }

    public String getAddressShot() {
        return addressShot;
    }

    public String getAddressFull() {
        return addressFull;
    }

    public String getDeliveryAreaId() {
        return deliveryAreaId;
    }

    @Override
    public String toString() {
        return "AddressData{" +
                "addressId='" + addressId + '\'' +
                ", addressShot='" + addressShot + '\'' +
                ", addressFull='" + addressFull + '\'' +
                ", deliveryAreaId='" + deliveryAreaId + '\'' +
                '}';
    }

}

