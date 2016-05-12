package ru.logistica.tms.dao.donutsDao;

import ru.logistica.tms.dao.ordersDao.Order;
import ru.logistica.tms.dao.suppliersDao.Supplier;

import java.sql.Date;
import java.util.Set;

public class Donut {
    private Integer donutId;
    private Date creationDate;
    private String comment;
    private String driver;
    private String driverPhoneNumber;
    private String licensePlate;
    private Integer palletsQty;
    private Supplier supplier;
    private Set<Order> orders;
}
