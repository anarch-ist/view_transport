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

    @OneToMany(mappedBy="warehouse", fetch = FetchType.EAGER)
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

        return warehouseId != null ? warehouseId.equals(warehouse.warehouseId) : warehouse.warehouseId == null;

    }

    @Override
    public int hashCode() {
        return warehouseId != null ? warehouseId.hashCode() : 0;
    }
}
