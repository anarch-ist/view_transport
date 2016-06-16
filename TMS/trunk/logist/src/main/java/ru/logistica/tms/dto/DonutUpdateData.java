package ru.logistica.tms.dto;

import ru.logistica.tms.util.JsonUtils;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DonutUpdateData {
    public final int donutDocPeriodId;
    public final long periodBegin;
    public final long periodEnd;
    public final String driver;
    public final String licensePlate;
    public final int palletsQty;
    public final String driverPhoneNumber;
    public final String commentForDonut;
    public final Set<OrderUpdateData> orders;
    public final Set<Integer> ordersIdForDelete;

    public DonutUpdateData(String jsonSting) throws ValidateDataException {
        try {
            JsonObject receivedJsonObject = JsonUtils.parseStringAsObject(jsonSting);
            this.donutDocPeriodId = receivedJsonObject.getInt("donutDocPeriodId");
            this.driver = receivedJsonObject.getString("driver");
            this.licensePlate = receivedJsonObject.getString("licensePlate");
            this.palletsQty = receivedJsonObject.getInt("palletsQty");
            this.driverPhoneNumber = receivedJsonObject.getString("driverPhoneNumber");
            this.commentForDonut = receivedJsonObject.getString("commentForDonut");
            this.periodBegin = receivedJsonObject.getJsonObject("period").getJsonNumber("periodBegin").longValueExact();
            this.periodEnd = receivedJsonObject.getJsonObject("period").getJsonNumber("periodEnd").longValueExact();

            Set<OrderUpdateData> ordersSet = new HashSet<>();
            JsonArray orders = receivedJsonObject.getJsonArray("orders");
            for (JsonValue orderAsJsonValue : orders) {
                if (!orderAsJsonValue.getValueType().equals(JsonValue.ValueType.OBJECT))
                    throw new ValidateDataException("data must be an object");
                JsonObject orderAsJsonObject = (JsonObject) orderAsJsonValue;

                Integer orderId;
                if (orderAsJsonObject.isNull("orderId")) {
                    orderId = null;
                } else {
                    orderId = orderAsJsonObject.getInt("orderId");
                }

                OrderUpdateData order = new OrderUpdateData(
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

            Set<Integer> ordersIdForDelete = new HashSet<>();
            JsonArray removedOrders = receivedJsonObject.getJsonArray("removedOrders");
            for (JsonValue removedOrder : removedOrders) {
                ordersIdForDelete.add(((JsonNumber) removedOrder).intValueExact());
            }
            this.ordersIdForDelete = ordersIdForDelete;


        } catch (Exception e) {
            throw new ValidateDataException(e);
        }
    }

    public DonutUpdateData(int donutDocPeriodId, long periodBegin, long periodEnd, String driver, String licensePlate, int palletsQty, String driverPhoneNumber, String commentForDonut, Set<OrderUpdateData> orders, Set<Integer> ordersIdForDelete) {
        this.donutDocPeriodId = donutDocPeriodId;
        this.periodBegin = periodBegin;
        this.periodEnd = periodEnd;
        this.driver = driver;
        this.licensePlate = licensePlate;
        this.palletsQty = palletsQty;
        this.driverPhoneNumber = driverPhoneNumber;
        this.commentForDonut = commentForDonut;
        this.orders = orders;
        this.ordersIdForDelete = ordersIdForDelete;
    }

    @Override
    public String toString() {
        return "DonutUpdateData{" +
                "donutDocPeriodId=" + donutDocPeriodId +
                ", periodBegin=" + periodBegin +
                ", periodEnd=" + periodEnd +
                ", driver='" + driver + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", palletsQty=" + palletsQty +
                ", driverPhoneNumber='" + driverPhoneNumber + '\'' +
                ", commentForDonut='" + commentForDonut + '\'' +
                ", orders=" + orders +
                ", ordersIdForDelete=" + ordersIdForDelete +
                '}';
    }

    public static class OrderUpdateData {
        public final Integer orderId; // can be null
        public final String orderNumber;
        public final int finalDestinationWarehouseId;
        public final int boxQty;
        public final String commentForStatus;
        public final String orderStatusId;

        public OrderUpdateData(Integer orderId, String orderNumber, int finalDestinationWarehouseId, int boxQty, String commentForStatus, String orderStatusId) {
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
