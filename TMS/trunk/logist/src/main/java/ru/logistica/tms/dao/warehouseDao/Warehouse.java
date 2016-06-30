package ru.logistica.tms.dao.warehouseDao;

import ru.logistica.tms.dao.docDao.Doc;
import ru.logistica.tms.dao.orderDao.Order;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "warehouses", schema = "public")
public class Warehouse {
    private Integer warehouseId;
    private String warehouseName;
    private RusTimeZoneAbbr rusTimeZoneAbbr;
    private Set<Doc> docs;
    private Set<Order> finalDestinationOrders;

    @Id
    @SequenceGenerator(name="warehouses_warehouseid_seq", sequenceName="warehouses_warehouseid_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "warehouses_warehouseid_seq")
    @Column(name = "warehouseid", updatable = false)
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


    @Basic
    @Column(name = "rustimezoneabbr", nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    public RusTimeZoneAbbr getRusTimeZoneAbbr() {
        return rusTimeZoneAbbr;
    }

    public void setRusTimeZoneAbbr(RusTimeZoneAbbr rusTimeZoneAbbr) {
        this.rusTimeZoneAbbr = rusTimeZoneAbbr;
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

        return warehouseId != null ? warehouseId.equals(warehouse.warehouseId) : warehouse.warehouseId == null;

    }

    @Override
    public int hashCode() {
        return warehouseId != null ? warehouseId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "warehouseId=" + warehouseId +
                ", warehouseName='" + warehouseName + '\'' +
                ", rusTimeZoneAbbr=" + rusTimeZoneAbbr +
                '}';
    }
}
