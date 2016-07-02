package ru.logistica.tms.dao.audit.orderAuditDao;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "orders_audit", schema = "audit")
public class OrdersAudit {
    private Long ordersauditid;

    @Id
    @SequenceGenerator(name="orders_audit_ordersauditid_seq", sequenceName="orders_audit_ordersauditid_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "orders_audit_ordersauditid_seq")
    @Column(name = "ordersauditid", updatable = false)
    public Long getOrdersauditid() {
        return ordersauditid;
    }

    public void setOrdersauditid(Long ordersauditid) {
        this.ordersauditid = ordersauditid;
    }

    private String operation;

    @Basic
    @javax.persistence.Column(name = "operation", nullable = false, length = -1)
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    private Date stamp;

    @Basic
    @javax.persistence.Column(name = "stamp", nullable = false)
    public Date getStamp() {
        return stamp;
    }

    public void setStamp(Date stamp) {
        this.stamp = stamp;
    }

    private Integer userid;

    @Basic
    @javax.persistence.Column(name = "userid", nullable = false)
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    private Integer orderid;

    @Basic
    @javax.persistence.Column(name = "orderid", nullable = true)
    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    private String ordernumber;

    @Basic
    @javax.persistence.Column(name = "ordernumber", nullable = false, length = 16)
    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    private Short boxqty;

    @Basic
    @javax.persistence.Column(name = "boxqty", nullable = false)
    public Short getBoxqty() {
        return boxqty;
    }

    public void setBoxqty(Short boxqty) {
        this.boxqty = boxqty;
    }

    private Integer finaldestinationwarehouseid;

    @Basic
    @javax.persistence.Column(name = "finaldestinationwarehouseid", nullable = false)
    public Integer getFinaldestinationwarehouseid() {
        return finaldestinationwarehouseid;
    }

    public void setFinaldestinationwarehouseid(Integer finaldestinationwarehouseid) {
        this.finaldestinationwarehouseid = finaldestinationwarehouseid;
    }

    private Long donutdocperiodid;

    @Basic
    @javax.persistence.Column(name = "donutdocperiodid", nullable = false)
    public Long getDonutdocperiodid() {
        return donutdocperiodid;
    }

    public void setDonutdocperiodid(Long donutdocperiodid) {
        this.donutdocperiodid = donutdocperiodid;
    }

    private String orderstatus;

    @Basic
    @javax.persistence.Column(name = "orderstatus", nullable = false, length = 32)
    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    private String commentforstatus;

    @Basic
    @javax.persistence.Column(name = "commentforstatus", nullable = false, length = -1)
    public String getCommentforstatus() {
        return commentforstatus;
    }

    public void setCommentforstatus(String commentforstatus) {
        this.commentforstatus = commentforstatus;
    }

    private Date orderdate;

    @Basic
    @javax.persistence.Column(name = "orderdate", nullable = true)
    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    private String invoicenumber;

    @Basic
    @javax.persistence.Column(name = "invoicenumber", nullable = true, length = 255)
    public String getInvoicenumber() {
        return invoicenumber;
    }

    public void setInvoicenumber(String invoicenumber) {
        this.invoicenumber = invoicenumber;
    }

    private Date invoicedate;

    @Basic
    @javax.persistence.Column(name = "invoicedate", nullable = true)
    public Date getInvoicedate() {
        return invoicedate;
    }

    public void setInvoicedate(Date invoicedate) {
        this.invoicedate = invoicedate;
    }

    private Date deliverydate;

    @Basic
    @javax.persistence.Column(name = "deliverydate", nullable = true)
    public Date getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(Date deliverydate) {
        this.deliverydate = deliverydate;
    }

    private Integer weight;

    @Basic
    @javax.persistence.Column(name = "weight", nullable = true)
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    private Integer volume;

    @Basic
    @javax.persistence.Column(name = "volume", nullable = true)
    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    private BigDecimal goodscost;

    @Basic
    @javax.persistence.Column(name = "goodscost", nullable = true, precision = 2)
    public BigDecimal getGoodscost() {
        return goodscost;
    }

    public void setGoodscost(BigDecimal goodscost) {
        this.goodscost = goodscost;
    }


}
