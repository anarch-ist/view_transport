package ru.logistica.tms.dao.docPeriodDao;

import ru.logistica.tms.dao.orderDao.Order;
import ru.logistica.tms.dao.supplierDao.Supplier;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "donut_doc_periods", schema = "public", catalog = "postgres")
@PrimaryKeyJoinColumn(name="donutdocperiodid")
public class DonutDocPeriod extends DocPeriod{
    private Date creationDate;
    private String commentForDonut;
    private String driver;
    private String driverPhoneNumber;
    private String licensePlate;
    private Short palletsQty;
    private Supplier supplier;
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
    public String toString() {
        return "DonutDocPeriod{" +
                super.toString() +
                "creationDate=" + creationDate +
                ", comment='" + commentForDonut + '\'' +
                ", driver='" + driver + '\'' +
                ", driverPhoneNumber='" + driverPhoneNumber + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", palletsQty=" + palletsQty +
                ", supplier=" + supplier +
                '}';
    }
}
