package ru.logistica.tms.dto;

import ru.logistica.tms.util.JsonUtils;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DonutInsertData {
    public final long periodBegin;
    public final long periodEnd;
    public final String driver;
    public final String licensePlate;
    public final int palletsQty;
    public final String driverPhoneNumber;
    public final String commentForDonut;
    public final Set<OrderInsertData> orders;

    public DonutInsertData(String jsonSting) throws ValidateDataException {
        try {
            JsonObject receivedJsonObject = JsonUtils.parseStringAsObject(jsonSting);
            this.driver = receivedJsonObject.getString("driver");
            this.licensePlate = receivedJsonObject.getString("licensePlate");
            this.palletsQty = receivedJsonObject.getInt("palletsQty");
            this.driverPhoneNumber = receivedJsonObject.getString("driverPhoneNumber");
            this.commentForDonut = receivedJsonObject.getString("commentForDonut");
            this.periodBegin = receivedJsonObject.getJsonObject("period").getJsonNumber("periodBegin").longValueExact();
            this.periodEnd = receivedJsonObject.getJsonObject("period").getJsonNumber("periodEnd").longValueExact();
            Set<OrderInsertData> ordersSet = new HashSet<>();
            JsonArray orders = receivedJsonObject.getJsonArray("orders");
            for (JsonValue orderAsJsonValue : orders) {
                if (!orderAsJsonValue.getValueType().equals(JsonValue.ValueType.OBJECT))
                    throw new ValidateDataException("data must be an object");
                JsonObject orderAsJsonObject = (JsonObject) orderAsJsonValue;
                OrderInsertData order = new OrderInsertData(
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

    public DonutInsertData(long periodBegin, long periodEnd, String driver, String licensePlate, int palletsQty, String driverPhoneNumber, String commentForDonut, Set<OrderInsertData> orders) {
        this.periodBegin = periodBegin;
        this.periodEnd = periodEnd;
        this.driver = driver;
        this.licensePlate = licensePlate;
        this.palletsQty = palletsQty;
        this.driverPhoneNumber = driverPhoneNumber;
        this.commentForDonut = commentForDonut;
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "DonutInsertData{" +
                "periodBegin=" + periodBegin +
                ", periodEnd=" + periodEnd +
                ", driver='" + driver + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", palletsQty=" + palletsQty +
                ", driverPhoneNumber='" + driverPhoneNumber + '\'' +
                ", commentForDonut='" + commentForDonut + '\'' +
                ", orders=" + orders +
                '}';
    }

    public static class OrderInsertData {
        public final String orderNumber;
        public final int finalDestinationWarehouseId;
        public final int boxQty;
        public final String commentForStatus;
        public final String orderStatusId;

        public OrderInsertData(String orderNumber, int finalDestinationWarehouseId, int boxQty, String commentForStatus, String orderStatusId) {
            Objects.requireNonNull(orderNumber);
            Objects.requireNonNull(commentForStatus);
            this.orderNumber = orderNumber;
            this.finalDestinationWarehouseId = finalDestinationWarehouseId;
            this.boxQty = boxQty;
            this.commentForStatus = commentForStatus;
            this.orderStatusId = orderStatusId;
        }

        @Override
        public String toString() {
            return "OrderInsertData{" +
                    " orderNumber='" + orderNumber + '\'' +
                    ", finalDestinationWarehouseId=" + finalDestinationWarehouseId +
                    ", boxQty=" + boxQty +
                    ", commentForStatus='" + commentForStatus + '\'' +
                    ", orderStatusId='" + orderStatusId + '\'' +
                    '}';
        }
    }
}
