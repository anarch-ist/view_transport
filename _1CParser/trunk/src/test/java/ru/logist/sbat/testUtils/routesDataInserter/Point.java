package ru.logist.sbat.testUtils.routesDataInserter;

import java.sql.Time;

public class Point {
    private Integer pointID;
    private String pointIDExternal;
    private String dataSourceID;
    private String pointName;
    private String region;
    private Integer timeZone;
    private Integer docs;
    private String comments;
    private Time openTime;
    private Time closeTime;
    private String district;
    private String locality;
    private String mailIndex;
    private String address;
    private String email;
    private String phoneNumber;
    private String responsiblePersonId;
    private String pointTypeId;

    public Integer getPointID() {
        return pointID;
    }

    public void setPointID(Integer pointID) {
        this.pointID = pointID;
    }

    public String getPointIDExternal() {
        return pointIDExternal;
    }

    public void setPointIDExternal(String pointIDExternal) {
        this.pointIDExternal = pointIDExternal;
    }

    public String getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(String dataSourceID) {
        this.dataSourceID = dataSourceID;
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

    public Integer getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Integer timeZone) {
        this.timeZone = timeZone;
    }

    public Integer getDocs() {
        return docs;
    }

    public void setDocs(Integer docs) {
        this.docs = docs;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Time getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Time openTime) {
        this.openTime = openTime;
    }

    public Time getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Time closeTime) {
        this.closeTime = closeTime;
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

    public String getPointTypeId() {
        return pointTypeId;
    }

    public void setPointTypeId(String pointTypeId) {
        this.pointTypeId = pointTypeId;
    }

    @Override
    public String toString() {
        return "PointData{" +
                "pointID=" + pointID +
                ", pointIDExternal='" + pointIDExternal + '\'' +
                ", dataSourceID='" + dataSourceID + '\'' +
                ", pointName='" + pointName + '\'' +
                ", region='" + region + '\'' +
                ", timeZone=" + timeZone +
                ", docs=" + docs +
                ", comments='" + comments + '\'' +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", district='" + district + '\'' +
                ", locality='" + locality + '\'' +
                ", mailIndex='" + mailIndex + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", responsiblePersonId='" + responsiblePersonId + '\'' +
                ", pointTypeId='" + pointTypeId + '\'' +
                '}';
    }
}
