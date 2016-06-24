package ru.logistica.tms.dao.orderDao;

import ru.logistica.tms.dao.docPeriodDao.DonutDocPeriod;
import ru.logistica.tms.dao.warehouseDao.Warehouse;

import javax.persistence.*;

@Entity
@Table(name = "orders", schema = "public")
public class Order {
    private Integer orderId;
    private String orderNumber;
    private Short boxQty;
    private OrderStatuses orderStatus;
    private String commentForStatus;
    private DonutDocPeriod donutDocPeriod;
    private Warehouse finalDestinationWarehouse;

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
