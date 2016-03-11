package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;

public class AddressData {
    private String addressId; //not null and not empty
    private String addressShot; // not null
    private String addressFull; // not null
    private String deliveryAreaId; // not null

    public AddressData(JSONObject updateAddress) {
        setAddressShot((String) updateAddress.get("addressShot"));
        setAddressFull((String) updateAddress.get("addressFull"));
        setAddressId((String) updateAddress.get("addressId"));
        setDeliveryAreaId((String) updateAddress.get("deliveryAreaId"));
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        Util.requireNonNullOrEmpty(addressId, "addressId");
        this.addressId = addressId;
    }

    public String getAddressShot() {
        return addressShot;
    }

    public void setAddressShot(String addressShot) {
        Util.requireNonNull(addressShot, "addressShot");
        this.addressShot = addressShot;
    }

    public String getAddressFull() {
        return addressFull;
    }

    public void setAddressFull(String addressFull) {
        Util.requireNonNull(addressFull, "addressFull");
        this.addressFull = addressFull;
    }

    public String getDeliveryAreaId() {
        return deliveryAreaId;
    }

    public void setDeliveryAreaId(String deliveryAreaId) {
        Util.requireNonNull(deliveryAreaId, "deliveryAreaId");
        this.deliveryAreaId = deliveryAreaId;
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
