package ru.logistica.tms.util;

import ru.logistica.tms.dao.orderDao.OrderStatuses;
import ru.logistica.tms.dao.userDao.UserRoles;

import java.util.HashMap;
import java.util.Map;

public class RusNames {
    private static final Map<OrderStatuses, String> orderStatusesConverter = new HashMap<>();
    static {
        orderStatusesConverter.put(OrderStatuses.CREATED, "СОЗДАН");
        orderStatusesConverter.put(OrderStatuses.ARRIVED, "ПРИБЫТИЕ НА ТЕРРИТОРИЮ");
        orderStatusesConverter.put(OrderStatuses.DELIVERED, "ДОСТАВЛЕН");
        orderStatusesConverter.put(OrderStatuses.ERROR, "ОШИБКА");
        orderStatusesConverter.put(OrderStatuses.CANCELLED_BY_SUPPLIER_USER, "УДАЛЕН ПОСТАВЩИКОМ");
        orderStatusesConverter.put(OrderStatuses.CANCELLED_BY_WAREHOUSE_USER, "УДАЛЕН НАЧАЛЬНИКОМ СКЛАДА");
        orderStatusesConverter.put(OrderStatuses.NO_ORDERS, "НЕТ ЗАЯВОК");
    }
    public static Map<OrderStatuses, String> getOrderStatusesConverter() {
        return orderStatusesConverter;
    }

    private static final Map<UserRoles, String> userRolesConverter = new HashMap<>();
    static {
        userRolesConverter.put(UserRoles.SUPPLIER_MANAGER, "ПОСТАВЩИК");
        userRolesConverter.put(UserRoles.WH_BOSS, "НАЧАЛЬНИК СКЛАДА");
        userRolesConverter.put(UserRoles.WH_SECURITY_OFFICER, "ОХРАННИК");
        userRolesConverter.put(UserRoles.WH_DISPATCHER, "ДИСПЕТЧЕР СКЛАДА");
        userRolesConverter.put(UserRoles.WH_SUPERVISOR, "РУКОВОДСТВО");
    }
    public static Map<UserRoles, String> getUserRolesConverter() {
        return userRolesConverter;
    }
}
