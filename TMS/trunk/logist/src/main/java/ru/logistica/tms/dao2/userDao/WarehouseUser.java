package ru.logistica.tms.dao2.userDao;

import ru.logistica.tms.dao2.warehouseDao.Warehouse;

import javax.persistence.*;

@Entity
@Table(name = "warehouse_users", schema = "public", catalog = "postgres")
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
}
