package ru.logistica.tms.dto;

import ru.logistica.tms.util.JsonUtils;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.HashSet;
import java.util.Set;

public class DonutData {
    public final Integer donutDocPeriodId; // can be null for insert operation
    public final long periodBegin;
    public final long periodEnd;
    public final String driver;
    public final String licensePlate;
    public final int palletsQty;
    public final String driverPhoneNumber;
    public final String commentForDonut;
    public final Set<OrderData> orders;
    public final Set<Integer> ordersIdForDelete; // can be null if no delete operations

    public DonutData(String jsonSting) throws ValidateDataException {
        try {
            JsonObject receivedJsonObject = JsonUtils.parseStringAsObject(jsonSting);
            if (receivedJsonObject.containsKey("donutDocPeriodId")) {
                this.donutDocPeriodId = receivedJsonObject.getInt("donutDocPeriodId");
            } else {
                this.donutDocPeriodId = null;
            }
            this.driver = receivedJsonObject.getString("driver");
            this.licensePlate = receivedJsonObject.getString("licensePlate");
            this.palletsQty = receivedJsonObject.getInt("palletsQty");
            this.driverPhoneNumber = receivedJsonObject.getString("driverPhoneNumber");
            this.commentForDonut = receivedJsonObject.getString("commentForDonut");
            this.periodBegin = receivedJsonObject.getJsonObject("period").getJsonNumber("periodBegin").longValueExact();
            this.periodEnd = receivedJsonObject.getJsonObject("period").getJsonNumber("periodEnd").longValueExact();

            Set<OrderData> ordersSet = new HashSet<>();
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

                OrderData order = new OrderData(
                        orderId,
                        orderAsJsonObject.getString("orderNumber"),
                        orderAsJsonObject.getInt("finalDestinationWarehouseId"),
                        orderAsJsonObject.getInt("boxQty"),
                        orderAsJsonObject.getString("commentForStatus"),
                        orderAsJsonObject.getString("orderStatusId"),
                        orderAsJsonObject.getString("invoiceNumber"),
                        orderAsJsonObject.getJsonNumber("goodsCost").bigDecimalValue(),
                        orderAsJsonObject.getInt("orderPalletsQty")
                        );
                ordersSet.add(order);
            }
            this.orders = ordersSet;

            if (receivedJsonObject.containsKey("removedOrders")) {
                Set<Integer> ordersIdForDelete = new HashSet<>();
                JsonArray removedOrders = receivedJsonObject.getJsonArray("removedOrders");
                for (JsonValue removedOrder : removedOrders) {
                    ordersIdForDelete.add(((JsonNumber) removedOrder).intValueExact());
                }
                this.ordersIdForDelete = ordersIdForDelete;
            } else {
                this.ordersIdForDelete = null;
            }

        } catch (Exception e) {
            throw new ValidateDataException(e);
        }
    }

    public DonutData(Integer donutDocPeriodId, long periodBegin, long periodEnd, String driver, String licensePlate, int palletsQty, String driverPhoneNumber, String commentForDonut, Set<OrderData> orders, Set<Integer> ordersIdForDelete) {
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
        return "DonutData{" +
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

}
