package ru.logistica.tms.dao.ordersDao;

import ru.logistica.tms.dao.warehouseDao.Warehouse;

import java.sql.Timestamp;
import java.util.Map;

public class Order {
    private Integer orderId;
    private String orderNumber;
    private Integer boxQty;
    private Warehouse finalDestinationWarehouse;
    private Map<Timestamp, OrderState> orderProgress;
}
