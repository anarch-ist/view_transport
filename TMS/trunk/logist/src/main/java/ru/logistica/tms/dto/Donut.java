package ru.logistica.tms.dto;

import ru.logistica.tms.dao.docPeriodDao.DonutDocPeriod;
import ru.logistica.tms.util.JsonUtils;

import javax.json.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Donut {
    //    public final String supplierName;
    public final Long donutDocPeriodId; // can be null
    public final String period;
    public final String driver;
    public final String licensePlate;
    public final int palletsQty;
    public final String driverPhoneNumber;
    public final String commentForDonut;
    public final Set<Order> orders;


    public Donut(String jsonSting) throws ValidateDataException {
        try {
            JsonObject receivedJsonObject = JsonUtils.parseStringAsObject(jsonSting);
            if (receivedJsonObject.containsKey("donutDocPeriodId") &&
                    receivedJsonObject.get("donutDocPeriodId").getValueType().equals(JsonValue.ValueType.NUMBER)) {
                this.donutDocPeriodId = receivedJsonObject.getJsonNumber("donutDocPeriodId").longValue();
            } else {
                this.donutDocPeriodId = null;
            }
            this.driver = receivedJsonObject.getString("driver");
            this.licensePlate = receivedJsonObject.getString("licensePlate");
            this.palletsQty = receivedJsonObject.getInt("palletsQty");
            this.driverPhoneNumber = receivedJsonObject.getString("driverPhoneNumber");
            this.commentForDonut = receivedJsonObject.getString("commentForDonut");
            this.period = receivedJsonObject.getString("period");
            Set<Order> ordersSet = new HashSet<>();
            JsonArray orders = receivedJsonObject.getJsonArray("orders");
            for (JsonValue orderAsJsonValue : orders) {
                if (!orderAsJsonValue.getValueType().equals(JsonValue.ValueType.OBJECT))
                    throw new ValidateDataException("data must be an object");
                JsonObject orderAsJsonObject = (JsonObject) orderAsJsonValue;

                Integer orderId;
                JsonValue orderIdAsJsonValue = orderAsJsonObject.get("orderId");
                JsonValue.ValueType orderIdValueType = orderIdAsJsonValue.getValueType();
                if (orderIdValueType == JsonValue.ValueType.NULL) {
                    orderId = null;
                } else {
                    orderId = ((JsonNumber) orderIdAsJsonValue).intValue();
                }
                Order order = new Order(
                        orderId,
                        orderAsJsonObject.getString("orderNumber"),
                        orderAsJsonObject.getInt("finalDestinationWarehouseId"),
                        orderAsJsonObject.getInt("boxQty"),
                        orderAsJsonObject.getString("commentForStatus"),
                        orderAsJsonObject.getString("orderStatusId")
                );
                ordersSet.add(order);
            }
            this.orders = ordersSet;

        } catch (Exception e) {
            throw new ValidateDataException(e);
        }
    }

    public Donut(Long donutDocPeriodId, String period, String driver, String licensePlate, int palletsQty, String driverPhoneNumber, String commentForDonut, Set<Order> orders) {
        this.donutDocPeriodId = donutDocPeriodId;
        this.period = period;
        this.driver = driver;
        this.licensePlate = licensePlate;
        this.palletsQty = palletsQty;
        this.driverPhoneNumber = driverPhoneNumber;
        this.commentForDonut = commentForDonut;
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Donut{" +
                "donutDocPeriodId=" + donutDocPeriodId +
                ", period='" + period + '\'' +
                ", driver='" + driver + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", palletsQty=" + palletsQty +
                ", driverPhoneNumber='" + driverPhoneNumber + '\'' +
                ", commentForDonut='" + commentForDonut + '\'' +
                ", orders=" + orders +
                '}';
    }

    public static class Order {
        public final Integer orderId; // can be null
        public final String orderNumber;
        public final int finalDestinationWarehouseId;
        public final int boxQty;
        public final String commentForStatus;
        public final String orderStatusId;

        public Order(Integer orderId, String orderNumber, int finalDestinationWarehouseId, int boxQty, String commentForStatus, String orderStatusId) {
            Objects.requireNonNull(orderNumber);
            Objects.requireNonNull(commentForStatus);
            this.orderId = orderId;
            this.orderNumber = orderNumber;
            this.finalDestinationWarehouseId = finalDestinationWarehouseId;
            this.boxQty = boxQty;
            this.commentForStatus = commentForStatus;
            this.orderStatusId = orderStatusId;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "orderId=" + orderId +
                    ", orderNumber='" + orderNumber + '\'' +
                    ", finalDestinationWarehouseId=" + finalDestinationWarehouseId +
                    ", boxQty=" + boxQty +
                    ", commentForStatus='" + commentForStatus + '\'' +
                    ", orderStatusId='" + orderStatusId + '\'' +
                    '}';
        }
    }
}
