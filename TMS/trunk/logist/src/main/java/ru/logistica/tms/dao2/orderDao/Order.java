package ru.logistica.tms.dao2.orderDao;

import ru.logistica.tms.dao2.docPeriodDao.DonutDocPeriod;
import ru.logistica.tms.dao2.warehouseDao.Warehouse;

import javax.persistence.*;

@Entity
@Table(name = "orders", schema = "public", catalog = "postgres")
public class Order {
    private Integer orderId;
    private String orderNumber;
    private Short boxQty;
    private String orderStatus;
    private String commentForStatus;
    private DonutDocPeriod donutDocPeriod;
    private Warehouse finalDestinationWarehouse;

    @ManyToOne
    @JoinColumn(name = "donutdocperiodid")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public DonutDocPeriod getDonutDocPeriod() {
        return donutDocPeriod;
    }

    public void setDonutDocPeriod(DonutDocPeriod donutDocPeriod) {
        this.donutDocPeriod = donutDocPeriod;
    }

    @ManyToOne
    @JoinColumn(name = "finaldestinationwarehouseid")
    public Warehouse getFinalDestinationWarehouse() {
        return finalDestinationWarehouse;
    }

    public void setFinalDestinationWarehouse(Warehouse finalDestinationWarehouse) {
        this.finalDestinationWarehouse = finalDestinationWarehouse;
    }

    @Id
    @Column(name = "orderid", nullable = false)
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
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

        if (orderId != null ? !orderId.equals(order.orderId) : order.orderId != null) return false;
        if (orderNumber != null ? !orderNumber.equals(order.orderNumber) : order.orderNumber != null) return false;
        if (boxQty != null ? !boxQty.equals(order.boxQty) : order.boxQty != null) return false;
        if (orderStatus != null ? !orderStatus.equals(order.orderStatus) : order.orderStatus != null) return false;
        if (commentForStatus != null ? !commentForStatus.equals(order.commentForStatus) : order.commentForStatus != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (orderNumber != null ? orderNumber.hashCode() : 0);
        result = 31 * result + (boxQty != null ? boxQty.hashCode() : 0);
        result = 31 * result + (orderStatus != null ? orderStatus.hashCode() : 0);
        result = 31 * result + (commentForStatus != null ? commentForStatus.hashCode() : 0);
        return result;
    }
}
