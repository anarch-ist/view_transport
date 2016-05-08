package ru.logistica.tms.dao.warehouseDao;

import java.util.Set;

public class Doc {
    private Integer docId;
    private String docName;
    private Warehouse warehouse;
    private Set<Period> periods;

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Set<Period> getPeriods() {
        // LAZY get periods
        return periods;
    }

    public void setPeriods(Set<Period> periods) {
        this.periods = periods;
    }

    @Override
    public String toString() {
        return "Doc{" +
                "docId=" + docId +
                ", docName='" + docName + '\'' +
                ", warehouse=" + warehouse +
                ", periods=" + periods +
                '}';
    }
}
