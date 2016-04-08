package ru.logistica.tms.dao;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class DonutLists {
    private Serializable donutId;
    private Date creationDate;
    private Integer palletsQty;
    private Integer supplierId;
    private String driver;
    private String driverPhoneNumber;
    private String licensePlate;
    private DonutStatuses status;
    private String comment;
    private Integer transitPointId;

    @Id
    @Column(name = "donutId")
    public Serializable getDonutId() {
        return donutId;
    }

    public void setDonutId(Serializable donutId) {
        this.donutId = donutId;
    }

    @Basic
    @Column(name = "creationDate")
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Basic
    @Column(name = "palletsQty")
    public Integer getPalletsQty() {
        return palletsQty;
    }

    public void setPalletsQty(Integer palletsQty) {
        this.palletsQty = palletsQty;
    }

    @Basic
    @Column(name = "supplierId")
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    @Basic
    @Column(name = "driver")
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @Basic
    @Column(name = "driverPhoneNumber")
    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    @Basic
    @Column(name = "licensePlate")
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    @Basic
    @Column(name = "status")
    public DonutStatuses getStatus() {
        return status;
    }

    public void setStatus(DonutStatuses status) {
        this.status = status;
    }

    @Basic
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "transitPointId")
    public Integer getTransitPointId() {
        return transitPointId;
    }

    public void setTransitPointId(Integer transitPointId) {
        this.transitPointId = transitPointId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DonutLists that = (DonutLists) o;

        if (donutId != null ? !donutId.equals(that.donutId) : that.donutId != null) return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        if (palletsQty != null ? !palletsQty.equals(that.palletsQty) : that.palletsQty != null) return false;
        if (supplierId != null ? !supplierId.equals(that.supplierId) : that.supplierId != null) return false;
        if (driver != null ? !driver.equals(that.driver) : that.driver != null) return false;
        if (driverPhoneNumber != null ? !driverPhoneNumber.equals(that.driverPhoneNumber) : that.driverPhoneNumber != null)
            return false;
        if (licensePlate != null ? !licensePlate.equals(that.licensePlate) : that.licensePlate != null) return false;
        if (status != that.status) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (transitPointId != null ? transitPointId.equals(that.transitPointId) : that.transitPointId == null) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = donutId != null ? donutId.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (palletsQty != null ? palletsQty.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        result = 31 * result + (driver != null ? driver.hashCode() : 0);
        result = 31 * result + (driverPhoneNumber != null ? driverPhoneNumber.hashCode() : 0);
        result = 31 * result + (licensePlate != null ? licensePlate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (transitPointId != null ? transitPointId.hashCode() : 0);
        return result;
    }
}
