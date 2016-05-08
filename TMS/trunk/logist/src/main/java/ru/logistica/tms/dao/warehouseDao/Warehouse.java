package ru.logistica.tms.dao.warehouseDao;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Warehouse warehouse = (Warehouse) o;

        if (warehouseId != null ? !warehouseId.equals(warehouse.warehouseId) : warehouse.warehouseId != null) return false;
        if (warehouseName != null ? !warehouseName.equals(warehouse.warehouseName) : warehouse.warehouseName != null) return false;
        if (region != null ? !region.equals(warehouse.region) : warehouse.region != null) return false;
        if (district != null ? !district.equals(warehouse.district) : warehouse.district != null) return false;
        if (locality != null ? !locality.equals(warehouse.locality) : warehouse.locality != null) return false;
        if (mailIndex != null ? !mailIndex.equals(warehouse.mailIndex) : warehouse.mailIndex != null) return false;
        if (address != null ? !address.equals(warehouse.address) : warehouse.address != null) return false;
        if (email != null ? !email.equals(warehouse.email) : warehouse.email != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(warehouse.phoneNumber) : warehouse.phoneNumber != null) return false;
        if (responsiblePersonId != null ? !responsiblePersonId.equals(warehouse.responsiblePersonId) : warehouse.responsiblePersonId != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = warehouseId != null ? warehouseId.hashCode() : 0;
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (district != null ? district.hashCode() : 0);
        result = 31 * result + (locality != null ? locality.hashCode() : 0);
        result = 31 * result + (mailIndex != null ? mailIndex.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (responsiblePersonId != null ? responsiblePersonId.hashCode() : 0);
        return result;
    }
}
