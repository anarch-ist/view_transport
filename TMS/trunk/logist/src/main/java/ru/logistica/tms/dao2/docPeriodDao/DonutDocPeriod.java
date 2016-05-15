package ru.logistica.tms.dao2.docPeriodDao;

import ru.logistica.tms.dao2.orderDao.Order;
import ru.logistica.tms.dao2.supplierDao.Supplier;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "donut_doc_periods", schema = "public", catalog = "postgres")
@PrimaryKeyJoinColumn(name="donutdocperiodid")
public class DonutDocPeriod extends DocPeriod{
    private Date creationDate;
    private String comment;
    private String driver;
    private String driverPhoneNumber;
    private String licensePlate;
    private Short palletsQty;
    private Supplier supplier;
    private Set<Order> orders;

    @Basic
    @Column(name = "creationdate", nullable = false)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Basic
    @Column(name = "comment", nullable = false, length = -1)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "driver", nullable = false, length = 255)
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @Basic
    @Column(name = "driverphonenumber", nullable = false, length = 12)
    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    @Basic
    @Column(name = "licenseplate", nullable = false, length = 9)
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    @Basic
    @Column(name = "palletsqty", nullable = false)
    public Short getPalletsQty() {
        return palletsQty;
    }

    public void setPalletsQty(Short palletsQty) {
        this.palletsQty = palletsQty;
    }

    @ManyToOne
    @JoinColumn(name = "supplierid", nullable = false)
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @OneToMany(mappedBy = "donutDocPeriod")
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DonutDocPeriod that = (DonutDocPeriod) o;

        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (driver != null ? !driver.equals(that.driver) : that.driver != null) return false;
        if (driverPhoneNumber != null ? !driverPhoneNumber.equals(that.driverPhoneNumber) : that.driverPhoneNumber != null)
            return false;
        if (licensePlate != null ? !licensePlate.equals(that.licensePlate) : that.licensePlate != null) return false;
        return palletsQty != null ? palletsQty.equals(that.palletsQty) : that.palletsQty == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (driver != null ? driver.hashCode() : 0);
        result = 31 * result + (driverPhoneNumber != null ? driverPhoneNumber.hashCode() : 0);
        result = 31 * result + (licensePlate != null ? licensePlate.hashCode() : 0);
        result = 31 * result + (palletsQty != null ? palletsQty.hashCode() : 0);
        return result;
    }


}
