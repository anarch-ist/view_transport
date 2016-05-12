package ru.logistica.tms.dao.suppliersDao;

public class Supplier {
    private Integer supplierID;
    private String inn;

    public Integer getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Integer supplierID) {
        this.supplierID = supplierID;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Supplier supplier = (Supplier) o;

        if (supplierID != null ? !supplierID.equals(supplier.supplierID) : supplier.supplierID != null) return false;
        return !(inn != null ? !inn.equals(supplier.inn) : supplier.inn != null);

    }

    @Override
    public int hashCode() {
        int result = supplierID != null ? supplierID.hashCode() : 0;
        result = 31 * result + (inn != null ? inn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "supplierID=" + supplierID +
                ", inn='" + inn + '\'' +
                '}';
    }
}
