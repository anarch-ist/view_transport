package ru.logistica.tms.dao.docPeriodDao;

import ru.logistica.tms.dao.orderDao.Order;
import ru.logistica.tms.dao.userDao.SupplierUser;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "donut_doc_periods", schema = "public")
@PrimaryKeyJoinColumn(name="donutdocperiodid")
public class DonutDocPeriod extends DocPeriod{
    private Date creationDate;
    private String commentForDonut;
    private String driver;
    private String driverPhoneNumber;
    private String licensePlate;
    private Short palletsQty;
    private Date lastModified;
    private SupplierUser supplierUser;
    private Set<Order> orders;

    @Basic
    @Column(name = "creationdate", nullable = false)
    @Temporal(TemporalType.DATE)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Basic
    @Column(name = "commentfordonut", nullable = false, length = -1)
    public String getCommentForDonut() {
        return commentForDonut;
    }

    public void setCommentForDonut(String commentForDonut) {
        this.commentForDonut = commentForDonut;
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
    @Column(name = "driverphonenumber", nullable = false, length = 15)
    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    @Basic
    @Column(name = "licenseplate", nullable = false, length = 32)
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

    @Basic
    @Column(name = "lastModified", nullable = true, updatable = false, insertable = false)
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @ManyToOne
    @JoinColumn(name = "supplieruserid", nullable = false)
    public SupplierUser getSupplierUser() {
        return supplierUser;
    }

    public void setSupplierUser(SupplierUser supplier) {
        this.supplierUser = supplier;
    }

    @OneToMany(mappedBy = "donutDocPeriod")
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "DonutDocPeriod{" +
                super.toString() +
                "creationDate=" + creationDate +
                ", comment='" + commentForDonut + '\'' +
                ", driver='" + driver + '\'' +
                ", driverPhoneNumber='" + driverPhoneNumber + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", palletsQty=" + palletsQty +
                ", supplierUser=" + supplierUser +
                '}';
    }
}
