package ru.logistica.tms.dao.donutsDao;

import ru.logistica.tms.dao.suppliersDao.Supplier;

import java.sql.Date;

public class Donut {
    private Integer donutId;
    private Date creationDate;
    private String comment;
    private String driver;
    private String driverPhoneNumber;
    private String licensePlate;
    private Integer palletsQty;
    private Supplier supplier;

    public Integer getDonutId() {
        return donutId;
    }

    public void setDonutId(Integer donutId) {
        this.donutId = donutId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Integer getPalletsQty() {
        return palletsQty;
    }

    public void setPalletsQty(Integer palletsQty) {
        this.palletsQty = palletsQty;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Donut donut = (Donut) o;

        if (donutId != null ? !donutId.equals(donut.donutId) : donut.donutId != null) return false;
        if (creationDate != null ? !creationDate.equals(donut.creationDate) : donut.creationDate != null) return false;
        if (comment != null ? !comment.equals(donut.comment) : donut.comment != null) return false;
        if (driver != null ? !driver.equals(donut.driver) : donut.driver != null) return false;
        if (driverPhoneNumber != null ? !driverPhoneNumber.equals(donut.driverPhoneNumber) : donut.driverPhoneNumber != null)
            return false;
        if (licensePlate != null ? !licensePlate.equals(donut.licensePlate) : donut.licensePlate != null) return false;
        if (palletsQty != null ? !palletsQty.equals(donut.palletsQty) : donut.palletsQty != null) return false;
        return !(supplier != null ? !supplier.equals(donut.supplier) : donut.supplier != null);

    }

    @Override
    public int hashCode() {
        int result = donutId != null ? donutId.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (driver != null ? driver.hashCode() : 0);
        result = 31 * result + (driverPhoneNumber != null ? driverPhoneNumber.hashCode() : 0);
        result = 31 * result + (licensePlate != null ? licensePlate.hashCode() : 0);
        result = 31 * result + (palletsQty != null ? palletsQty.hashCode() : 0);
        result = 31 * result + (supplier != null ? supplier.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Donut{" +
                "donutId=" + donutId +
                ", creationDate=" + creationDate +
                ", comment='" + comment + '\'' +
                ", driver='" + driver + '\'' +
                ", driverPhoneNumber='" + driverPhoneNumber + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", palletsQty=" + palletsQty +
                ", supplier=" + supplier +
                '}';
    }
}
