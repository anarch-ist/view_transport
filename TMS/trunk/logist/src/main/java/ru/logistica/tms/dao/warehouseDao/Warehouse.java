package ru.logistica.tms.dao.warehouseDao;

import java.util.Set;

public class Warehouse {
    private Integer warehouseId;
    private String warehouseName;
    private String region;
    private String district;
    private String locality;
    private String mailIndex;
    private String address;
    private String email;
    private String phoneNumber;
    private String responsiblePersonId;
    private Set<Doc> docs;

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getMailIndex() {
        return mailIndex;
    }

    public void setMailIndex(String mailIndex) {
        this.mailIndex = mailIndex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getResponsiblePersonId() {
        return responsiblePersonId;
    }

    public void setResponsiblePersonId(String responsiblePersonId) {
        this.responsiblePersonId = responsiblePersonId;
    }

    public Set<Doc> getDocs() {
        return docs; // lazy get docs
    }

    public void setDocs(Set<Doc> docs) {
        this.docs = docs;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "warehouseId=" + warehouseId +
                ", warehouseName='" + warehouseName + '\'' +
                ", region='" + region + '\'' +
                ", district='" + district + '\'' +
                ", locality='" + locality + '\'' +
                ", mailIndex='" + mailIndex + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", responsiblePersonId='" + responsiblePersonId + '\'' +
                ", docs=" + docs +
                '}';
    }
}
