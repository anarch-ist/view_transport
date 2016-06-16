package ru.logistica.tms.dao.orderDao;

// BINDING ddl.sql
public enum OrderStatuses {
    CREATED, ARRIVED, CANCELLED_BY_WAREHOUSE_USER, CANCELLED_BY_SUPPLIER_USER, ERROR, DELIVERED
}
