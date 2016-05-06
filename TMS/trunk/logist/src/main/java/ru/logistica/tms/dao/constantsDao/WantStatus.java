package ru.logistica.tms.dao.constantsDao;


public class WantStatus {
    private String wantStatusId;
    private String wantStatusRusName;

    public String getWantStatusId() {
        return wantStatusId;
    }

    public void setWantStatusId(String wantStatusId) {
        this.wantStatusId = wantStatusId;
    }

    public String getWantStatusRusName() {
        return wantStatusRusName;
    }

    public void setWantStatusRusName(String wantStatusRusName) {
        this.wantStatusRusName = wantStatusRusName;
    }

    @Override
    public String toString() {
        return "WantStatus{" +
                "wantStatusId='" + wantStatusId + '\'' +
                ", wantStatusRusName='" + wantStatusRusName + '\'' +
                '}';
    }
}
