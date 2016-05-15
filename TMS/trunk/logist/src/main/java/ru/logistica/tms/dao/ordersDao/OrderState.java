package ru.logistica.tms.dao.ordersDao;

import ru.logistica.tms.dao.Binding;
import ru.logistica.tms.dao.donutsDao.Donut;

public class OrderState {
    private OrderStatus orderStatus;
    private String commentForStatus;
    private Donut donut;

    @Binding(sqlObject = "order_statuses")
    enum OrderStatus {
        CREATED("CREATED"),
        CANCELLED_BY_WAREHOUSE_USER("CANCELLED_BY_WAREHOUSE_USER"),
        CANCELLED_BY_SUPPLIER_USER("CANCELLED_BY_SUPPLIER_USER"),
        ERROR("ERROR"),
        DELIVERED("DELIVERED");
        private String name;
        OrderStatus(String name) {this.name = name;}
        @Override
        public String toString() {
            return name;
        }
    }



}
