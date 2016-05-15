package ru.logistica.tms.dao2.warehouseDao;

import ru.logistica.tms.dao2.docDao.Doc;
import ru.logistica.tms.dao2.orderDao.Order;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "warehouses", schema = "public", catalog = "postgres")
public class Warehouse {
    private Integer warehouseId;
    private String warehouseName;
    private Set<Doc> docs;
    private Set<Order> finalDestinationOrders;

    @Id
    @Column(name = "warehouseid", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "warehousename", nullable = false, length = 128)
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @OneToMany(mappedBy="warehouse")
    public Set<Doc> getDocs() {
        return docs;
    }

    public void setDocs(Set<Doc> docs) {
        this.docs = docs;
    }

    @OneToMany(mappedBy="finalDestinationWarehouse")
    public Set<Order> getFinalDestinationOrders() {
        return finalDestinationOrders;
    }

    public void setFinalDestinationOrders(Set<Order> finalDestinationOrders) {
        this.finalDestinationOrders = finalDestinationOrders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Warehouse warehouse = (Warehouse) o;

        if (warehouseId != null ? !warehouseId.equals(warehouse.warehouseId) : warehouse.warehouseId != null)
            return false;
        if (warehouseName != null ? !warehouseName.equals(warehouse.warehouseName) : warehouse.warehouseName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = warehouseId != null ? warehouseId.hashCode() : 0;
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        return result;
    }


}
