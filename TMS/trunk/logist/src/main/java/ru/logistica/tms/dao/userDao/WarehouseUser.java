package ru.logistica.tms.dao.userDao;

import ru.logistica.tms.dao.warehouseDao.Warehouse;

import javax.persistence.*;

@Entity
@Table(name = "warehouse_users", schema = "public")
@PrimaryKeyJoinColumn(name="userid")
public class WarehouseUser extends User{
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "warehouseid", nullable = false)
    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public String toString() {
        return "WarehouseUser{" +
                super.toString() +
                "warehouse=" + warehouse +
                '}';
    }
}
