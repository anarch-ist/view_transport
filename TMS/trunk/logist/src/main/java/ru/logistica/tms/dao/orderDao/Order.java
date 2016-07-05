package ru.logistica.tms.dao.orderDao;

import ru.logistica.tms.dao.docPeriodDao.DonutDocPeriod;
import ru.logistica.tms.dao.warehouseDao.Warehouse;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "orders", schema = "public")
public class Order {
    private Integer orderId;
    private String orderNumber;
    private Short boxQty;
    private Warehouse finalDestinationWarehouse;
    private DonutDocPeriod donutDocPeriod;
    private OrderStatuses orderStatus;
    private String commentForStatus;
    private String invoiceNumber;
    private BigDecimal goodsCost;
    private Short orderPalletsQty;
    private Date lastModified;

    @Id
    @SequenceGenerator(name="orders_orderid_seq", sequenceName="orders_orderid_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "orders_orderid_seq")
    @Column(name = "orderid", updatable = false)
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @ManyToOne
    @JoinColumn(name = "donutdocperiodid", nullable = false)
    public DonutDocPeriod getDonutDocPeriod() {
        return donutDocPeriod;
    }

    public void setDonutDocPeriod(DonutDocPeriod donutDocPeriod) {
        this.donutDocPeriod = donutDocPeriod;
    }

    @ManyToOne
    @JoinColumn(name = "finaldestinationwarehouseid", nullable = false)
    public Warehouse getFinalDestinationWarehouse() {
        return finalDestinationWarehouse;
    }

    public void setFinalDestinationWarehouse(Warehouse finalDestinationWarehouse) {
        this.finalDestinationWarehouse = finalDestinationWarehouse;
    }

    @Basic
    @Column(name = "ordernumber", nullable = false, length = 16)
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Basic
    @Column(name = "boxqty", nullable = false)
    public Short getBoxQty() {
        return boxQty;
    }

    public void setBoxQty(Short boxQty) {
        this.boxQty = boxQty;
    }

    @Basic
    @Column(name = "orderstatus", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    public OrderStatuses getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatuses orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Basic
    @Column(name = "commentforstatus", nullable = false, length = -1)
    public String getCommentForStatus() {
        return commentForStatus;
    }

    public void setCommentForStatus(String commentForStatus) {
        this.commentForStatus = commentForStatus;
    }

    @Basic
    @Column(name = "invoicenumber", nullable = false, length = 255)
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @Basic
    @Column(name = "goodscost", nullable = false, length = 12, precision = 2)
    public BigDecimal getGoodsCost() {
        return goodsCost;
    }

    public void setGoodsCost(BigDecimal goodsCost) {
        this.goodsCost = goodsCost;
    }

    @Basic
    @Column(name = "orderpalletsqty", nullable = false)
    public Short getOrderPalletsQty() {
        return orderPalletsQty;
    }

    public void setOrderPalletsQty(Short orderPalletsQty) {
        this.orderPalletsQty = orderPalletsQty;
    }

    @Basic
    @Column(name = "lastModified", nullable = true, updatable = false, insertable = false)
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return orderId != null ? orderId.equals(order.orderId) : order.orderId == null;

    }

    @Override
    public int hashCode() {
        return orderId != null ? orderId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", boxQty=" + boxQty +
                ", orderStatus=" + orderStatus +
                ", commentForStatus='" + commentForStatus + '\'' +
                ", finalDestinationWarehouse=" + finalDestinationWarehouse +
                '}';
    }
}
