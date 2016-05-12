package ru.logistica.tms.dao.warehouseDao;

import java.util.Set;

public class Warehouse {
    private Integer warehouseId;
    private String warehouseName;
    private Set<Doc> docs;

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Set<Doc> getDocs() {
        return docs; // lazy get docs
    }

    public void setDocs(Set<Doc> docs) {
        this.docs = docs;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "warehouseId=" + warehouseId +
                ", warehouseName='" + warehouseName + '\'' +
                ", docs=" + docs +
                '}';
    }
}
