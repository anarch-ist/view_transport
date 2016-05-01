package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.jsonReader.Util;
import ru.logist.sbat.jsonParser.jsonReader.ValidatorException;

import java.time.format.DateTimeFormatter;
import java.sql.Date;

public class RequestsData {
    public static final String FN_REQUEST_ID = "requestId";
    public static final String FN_REQUEST_NUMBER = "requestNumber";
    public static final String FN_REQUEST_DATE = "requestDate";
    public static final String FN_INVOICE_NUMBER = "invoiceNumber";
    public static final String FN_INVOICE_DATE = "invoiceDate";
    public static final String FN_DOCUMENT_NUMBER = "documentNumber";
    public static final String FN_DOCUMENT_DATE = "documentDate";
    public static final String FN_FIRMA = "firma";
    public static final String FN_STORAGE = "storage";
    public static final String FN_CLIENT_ID = "clientId";
    public static final String FN_ADDRESS_ID = "addressId";
    public static final String FN_CONTACT_NAME = "contactName";
    public static final String FN_CONTACT_PHONE = "contactPhone";
    public static final String FN_DELIVERY_OPTION = "deliveryOption";
    public static final String FN_TRADER_ID = "traderId";
    public static final String FN_DELIVERY_DATE = "deliveryDate";

    private static final DateTimeFormatter isoFormatter = DateTimeFormatter.BASIC_ISO_DATE;

    @Unique
    private String requestId;
    private String requestNumber;
    private Date requestDate;
    private String invoiceNumber;
    private Date invoiceDate;
    private String documentNumber;
    private Date documentDate;
    private String firma;
    private String storage;
    private String clientId;
    private String addressId;
    private String contactName;
    private String contactPhone;
    private String deliveryOption;
    private String traderId;
    private Date deliveryDate;

    public RequestsData(JSONObject updateRequests) throws ValidatorException {
        // check fields
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_REQUEST_ID, updateRequests);
        Util.checkFieldAvailableAndNotNull           (FN_REQUEST_NUMBER, updateRequests);
        Util.checkFieldAvailableAndNotNull           (FN_REQUEST_DATE, updateRequests);
        Util.checkFieldAvailableAndNotNull           (FN_INVOICE_NUMBER, updateRequests);
        Util.checkFieldAvailableAndNotNull           (FN_INVOICE_DATE, updateRequests);
        Util.checkFieldAvailableAndNotNull           (FN_DOCUMENT_NUMBER, updateRequests);
        Util.checkFieldAvailableAndNotNull           (FN_DOCUMENT_DATE, updateRequests);
        Util.checkFieldAvailableAndNotNull           (FN_FIRMA, updateRequests);
        Util.checkFieldAvailableAndNotNull           (FN_STORAGE, updateRequests);
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_CLIENT_ID, updateRequests);
        Util.checkFieldAvailableAndNotNull           (FN_ADDRESS_ID, updateRequests);
        Util.checkFieldAvailableAndNotNull           (FN_CONTACT_NAME, updateRequests);
        Util.checkFieldAvailableAndNotNull           (FN_CONTACT_PHONE, updateRequests);
        Util.checkFieldAvailableAndNotNull           (FN_DELIVERY_OPTION, updateRequests);
        Util.checkFieldAvailableAndNotNullAndNotEmpty(FN_TRADER_ID, updateRequests);
        Util.checkFieldAvailableAndNotNull           (FN_DELIVERY_DATE, updateRequests);

        //set values
        Util.setStringValue              (FN_REQUEST_ID, updateRequests, this, "requestId");
        Util.setStringValue              (FN_REQUEST_NUMBER, updateRequests, this, "requestNumber");
        Util.setNullIfEmptyOrSetValueDate(FN_REQUEST_DATE, updateRequests, this, "requestDate", isoFormatter);
        Util.setStringValue              (FN_INVOICE_NUMBER, updateRequests, this, "invoiceNumber");
        Util.setNullIfEmptyOrSetValueDate(FN_INVOICE_DATE, updateRequests, this, "invoiceDate", isoFormatter);
        Util.setStringValue              (FN_DOCUMENT_NUMBER, updateRequests, this, "documentNumber");
        Util.setNullIfEmptyOrSetValueDate(FN_DOCUMENT_DATE, updateRequests, this, "documentDate", isoFormatter);
        Util.setStringValue              (FN_FIRMA, updateRequests, this, "firma");
        Util.setStringValue              (FN_STORAGE, updateRequests, this, "storage");
        Util.setStringValue              (FN_CLIENT_ID, updateRequests, this, "clientId");
        Util.setStringValue              (FN_ADDRESS_ID, updateRequests, this, "addressId");
        Util.setStringValue              (FN_CONTACT_NAME, updateRequests, this, "contactName");
        Util.setStringValue              (FN_CONTACT_PHONE, updateRequests, this, "contactPhone");
        Util.setStringValue              (FN_DELIVERY_OPTION, updateRequests, this, "deliveryOption");
        Util.setStringValue              (FN_TRADER_ID, updateRequests, this, "traderId");
        Util.setNullIfEmptyOrSetValueDate(FN_DELIVERY_DATE, updateRequests, this, "deliveryDate", isoFormatter);
    }

    public String getRequestId() {
        return requestId;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getTraderId() {
        return traderId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public String getFirma() {
        return firma;
    }

    public String getStorage() {
        return storage;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    @Override
    public String toString() {
        return "RequestsData{" +
                "requestId='" + requestId + '\'' +
                ", requestNumber='" + requestNumber + '\'' +
                ", requestDate=" + requestDate +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", invoiceDate=" + invoiceDate +
                ", documentNumber='" + documentNumber + '\'' +
                ", documentDate=" + documentDate +
                ", firma='" + firma + '\'' +
                ", storage='" + storage + '\'' +
                ", clientId='" + clientId + '\'' +
                ", addressId='" + addressId + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", deliveryOption='" + deliveryOption + '\'' +
                ", traderId='" + traderId + '\'' +
                ", deliveryDate=" + deliveryDate +
                '}';
    }
}
