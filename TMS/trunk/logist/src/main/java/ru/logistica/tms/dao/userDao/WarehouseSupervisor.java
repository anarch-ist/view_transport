package ru.logistica.tms.dao.userDao;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "warehouse_supervisors", schema = "public")
@PrimaryKeyJoinColumn(name="userid")
public class WarehouseSupervisor extends User {

    @Override
    public String toString() {
        return "WarehouseSupervisor{" +
                super.toString() +
                '}';
    }

}
