package ru.logistica.tms.dao.audit.docPeriodAuditDao;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "donut_doc_periods_audit", schema = "audit")
public class DonutDocPeriodAudit {
    private Long donutdocperiodsauditid;
    private String operation;
    private Date stamp;
    private Integer userid;
    private Long donutdocperiodid;
    private Date creationdate;
    private String commentfordonut;
    private String driver;
    private String driverphonenumber;
    private String licenseplate;
    private Integer palletsqty;
    private Integer supplieruserid;

    @Id
    @SequenceGenerator(name="donut_doc_periods_audit_donutdocperiodsauditid_seq", sequenceName="donut_doc_periods_audit_donutdocperiodsauditid_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "donut_doc_periods_audit_donutdocperiodsauditid_seq")
    @Column(name = "donutdocperiodsauditid", updatable = false)
    public Long getDonutdocperiodsauditid() {
        return donutdocperiodsauditid;
    }

    public void setDonutdocperiodsauditid(Long donutdocperiodsauditid) {
        this.donutdocperiodsauditid = donutdocperiodsauditid;
    }

    @Basic
    @Column(name = "operation", nullable = false, length = -1)
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Basic
    @Column(name = "stamp", nullable = false)
    public Date getStamp() {
        return stamp;
    }

    public void setStamp(Date stamp) {
        this.stamp = stamp;
    }

    @Basic
    @Column(name = "userid", nullable = false)
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "donutdocperiodid", nullable = false)
    public Long getDonutdocperiodid() {
        return donutdocperiodid;
    }

    public void setDonutdocperiodid(Long donutdocperiodid) {
        this.donutdocperiodid = donutdocperiodid;
    }

    @Basic
    @Column(name = "creationdate", nullable = false)
    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    @Basic
    @Column(name = "commentfordonut", nullable = false, length = -1)
    public String getCommentfordonut() {
        return commentfordonut;
    }

    public void setCommentfordonut(String commentfordonut) {
        this.commentfordonut = commentfordonut;
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
    public String getDriverphonenumber() {
        return driverphonenumber;
    }

    public void setDriverphonenumber(String driverphonenumber) {
        this.driverphonenumber = driverphonenumber;
    }

    @Basic
    @Column(name = "licenseplate", nullable = false, length = 9)
    public String getLicenseplate() {
        return licenseplate;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }

    @Basic
    @Column(name = "palletsqty", nullable = false)
    public Integer getPalletsqty() {
        return palletsqty;
    }

    public void setPalletsqty(Integer palletsqty) {
        this.palletsqty = palletsqty;
    }

    @Basic
    @Column(name = "supplieruserid", nullable = false)
    public Integer getSupplieruserid() {
        return supplieruserid;
    }

    public void setSupplieruserid(Integer supplieruserid) {
        this.supplieruserid = supplieruserid;
    }

}
