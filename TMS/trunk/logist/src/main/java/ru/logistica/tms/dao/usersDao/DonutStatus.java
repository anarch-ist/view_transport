package ru.logistica.tms.dao.usersDao;


public class DonutStatus {
    private String donutStatusId;
    private String donutStatusRusName;

    public String getDonutStatusId() {
        return donutStatusId;
    }

    public void setDonutStatusId(String donutStatusId) {
        this.donutStatusId = donutStatusId;
    }

    public String getDonutStatusRusName() {
        return donutStatusRusName;
    }

    public void setDonutStatusRusName(String donutStatusRusName) {
        this.donutStatusRusName = donutStatusRusName;
    }

    @Override
    public String toString() {
        return "DonutStatus{" +
                "donutStatusId='" + donutStatusId + '\'' +
                ", donutStatusRusName='" + donutStatusRusName + '\'' +
                '}';
    }
}
