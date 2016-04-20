package ru.logistica.tms.dao.pointsDao;

import java.io.Serializable;

public class Point {
    private Serializable pointId;
    private String pointName;
    private String region;
    private String district;
    private String locality;
    private String mailIndex;
    private String address;
    private String email;
    private String phoneNumber;
    private String responsiblePersonId;

    public Serializable getPointId() {
        return pointId;
    }

    public void setPointId(Serializable pointId) {
        this.pointId = pointId;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
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

        Point points = (Point) o;

        if (pointId != null ? !pointId.equals(points.pointId) : points.pointId != null) return false;
        if (pointName != null ? !pointName.equals(points.pointName) : points.pointName != null) return false;
        if (region != null ? !region.equals(points.region) : points.region != null) return false;
        if (district != null ? !district.equals(points.district) : points.district != null) return false;
        if (locality != null ? !locality.equals(points.locality) : points.locality != null) return false;
        if (mailIndex != null ? !mailIndex.equals(points.mailIndex) : points.mailIndex != null) return false;
        if (address != null ? !address.equals(points.address) : points.address != null) return false;
        if (email != null ? !email.equals(points.email) : points.email != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(points.phoneNumber) : points.phoneNumber != null) return false;
        if (responsiblePersonId != null ? !responsiblePersonId.equals(points.responsiblePersonId) : points.responsiblePersonId != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = pointId != null ? pointId.hashCode() : 0;
        result = 31 * result + (pointName != null ? pointName.hashCode() : 0);
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
