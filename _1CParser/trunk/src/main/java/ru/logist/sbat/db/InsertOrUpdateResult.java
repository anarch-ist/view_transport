package ru.logist.sbat.db;

public class InsertOrUpdateResult {

    private String server;
    private Integer packageNumber;
    private String status;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(Integer packageNumber) {
        this.packageNumber = packageNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return server + ";" + packageNumber + ";" + status;
    }
}
