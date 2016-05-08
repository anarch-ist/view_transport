package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.warehouseDao.Warehouse;

public class WarehouseUser extends PrivelegedUser {
    private Warehouse warehouse;

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public String toString() {
        return super.toString() + ", warehouse='" + warehouse + "\'";
    }
}
