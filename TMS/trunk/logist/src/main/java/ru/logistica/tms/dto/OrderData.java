package ru.logistica.tms.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderData {
    public final Integer orderId; // can be null if insert, else update
    public final String orderNumber;
    public final int finalDestinationWarehouseId;
    public final int boxQty;
    public final String commentForStatus;
    public final String orderStatusId;
    public final String invoiceNumber;
    public final BigDecimal goodsCost;
    public final int orderPalletsQty;

    public OrderData(Integer orderId, String orderNumber, int finalDestinationWarehouseId, int boxQty, String commentForStatus, String orderStatusId, String invoiceNumber, BigDecimal goodsCost, int orderPalletsQty) {
        Objects.requireNonNull(orderNumber);
        Objects.requireNonNull(commentForStatus);
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.finalDestinationWarehouseId = finalDestinationWarehouseId;
        this.boxQty = boxQty;
        this.commentForStatus = commentForStatus;
        this.orderStatusId = orderStatusId;
        this.invoiceNumber = invoiceNumber;
        this.goodsCost = goodsCost;
        this.orderPalletsQty = orderPalletsQty;
    }

    @Override
    public String toString() {
        return "OrderData{" +
                "orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", finalDestinationWarehouseId=" + finalDestinationWarehouseId +
                ", boxQty=" + boxQty +
                ", commentForStatus='" + commentForStatus + '\'' +
                ", orderStatusId='" + orderStatusId + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", goodsCost=" + goodsCost +
                ", orderPalletsQty=" + orderPalletsQty +
                '}';
    }
}
