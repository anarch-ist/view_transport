package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.Util;

import java.time.format.DateTimeFormatter;
import java.sql.Date;

public class RequestsData {
    private static final DateTimeFormatter isoFormatter = DateTimeFormatter.BASIC_ISO_DATE;
    private String requestId;
    private String requestNumber;
    private Date requestDate;
    private String clientId;
    private String addressId;
    private String traderId;
    private String invoiceNumber;
    private Date invoiceDate;
    private String documentNumber;
    private Date documentDate;
    private String firma;
    private String storage;
    private String contactName;
    private String contactPhone;
    private String deliveryOption;
    private Date deliveryDate;

    public RequestsData(JSONObject updateRequests) {
        setRequestId((String) updateRequests.get("requestId"));
        setRequestNumber((String) updateRequests.get("requestNumber"));
        setRequestDate((String) updateRequests.get("requestDate"));
        setClientId((String) updateRequests.get("clientId"));
        setAddressId((String) updateRequests.get("addressId"));
        setTraderId((String) updateRequests.get("traderId"));
        setInvoiceNumber((String) updateRequests.get("invoiceNumber"));
        setInvoiceDate((String) updateRequests.get("invoiceDate"));
        setDocumentNumber((String) updateRequests.get("documentNumber"));
        setDocumentDate((String) updateRequests.get("documentDate"));
        setFirma((String) updateRequests.get("firma"));
        setStorage((String) updateRequests.get("storage"));
        setContactName((String) updateRequests.get("contactName"));
        setContactPhone((String) updateRequests.get("contactPhone"));
        setDeliveryOption((String) updateRequests.get("deliveryOption"));
        setDeliveryDate((String) updateRequests.get("deliveryDate"));
    }

    public String getRequestId() {
        return requestId;
    }

    private void setRequestId(String requestId) {
        Util.requireNonNullOrEmpty(requestId, "requestId");
        this.requestId = requestId;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    private void setRequestNumber(String requestNumber) {
        Util.requireNonNull(requestNumber, "requestNumber");
        this.requestNumber = requestNumber;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    private void setRequestDate(String requestDateAsString) {
        requestDate = Util.getDateWithCheck(requestDateAsString, "requestDate", isoFormatter);
    }

    public String getClientId() {
        return clientId;
    }

    private void setClientId(String clientId) {
        Util.requireNonNullOrEmpty(clientId, "clientId");
        this.clientId = clientId;
    }

    public String getAddressId() {
        return addressId;
    }

    private void setAddressId(String addressId) {
        Util.requireNonNullOrEmpty(addressId, "addressId");
        this.addressId = addressId;
    }

    public String getTraderId() {
        return traderId;
    }

    private void setTraderId(String traderId) {
        Util.requireNonNullOrEmpty(traderId, "traderId");
        this.traderId = traderId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    private void setInvoiceNumber(String invoiceNumber) {
        Util.requireNonNull(invoiceNumber, "invoiceNumber");
        this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    private void setInvoiceDate(String invoiceDateAsString) {
        invoiceDate = Util.getDateWithCheck(invoiceDateAsString, "invoiceDate", isoFormatter);
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    // TODO fix this
    private void setDocumentNumber(String documentNumber) {
        //Util.requireNonNull(documentNumber, "documentNumber");
        if (documentNumber == null) {
            this.documentNumber = "";
            return;
        }
        this.documentNumber = documentNumber;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    //TODO fix this
    private void setDocumentDate(String documentDateAsString) {
        //documentDate = Util.getDateWithCheck(documentDateAsString, "documentDate", isoFormatter);
    }

    public String getFirma() {
        return firma;
    }

    private void setFirma(String firma) {
        Util.requireNonNull(firma, "firma");
        this.firma = firma;
    }

    public String getStorage() {
        return storage;
    }

    private void setStorage(String storage) {
        Util.requireNonNull(storage, "storage");
        this.storage = storage;
    }

    public String getContactName() {
        return contactName;
    }

    private void setContactName(String contactName) {
        Util.requireNonNull(contactName, "contactName");
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    private void setContactPhone(String contactPhone) {
        Util.requireNonNull(contactPhone, "contactPhone");
        this.contactPhone = contactPhone;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    private void setDeliveryOption(String deliveryOption) {
        Util.requireNonNull(deliveryOption, "deliveryOption");
        this.deliveryOption = deliveryOption;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    private void setDeliveryDate(String deliveryDateAsString) {
        deliveryDate = Util.getDateWithCheck(deliveryDateAsString, "deliveryDate", isoFormatter);
    }

    @Override
    public String toString() {
        return "RequestsData{" +
                "requestId='" + requestId + '\'' +
                ", requestNumber='" + requestNumber + '\'' +
                ", requestDate=" + requestDate +
                ", clientId='" + clientId + '\'' +
                ", addressId='" + addressId + '\'' +
                ", traderId='" + traderId + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", invoiceDate=" + invoiceDate +
                ", documentNumber='" + documentNumber + '\'' +
                ", documentDate=" + documentDate +
                ", firma='" + firma + '\'' +
                ", storage='" + storage + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", deliveryOption='" + deliveryOption + '\'' +
                ", deliveryDate=" + deliveryDate +
                '}';
    }
}
