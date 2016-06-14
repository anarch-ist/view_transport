package ru.logistica.tms.dao.supplierDao;

import javax.persistence.*;

@Entity
@Table(name = "suppliers", schema = "public", catalog = "postgres")
public class Supplier {
    private Integer supplierId;
    private String inn;

    @Id
    @SequenceGenerator(name="suppliers_supplierid_seq", sequenceName="suppliers_supplierid_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "suppliers_supplierid_seq")
    @Column(name = "supplierid", updatable = false)
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    @Basic
    @Column(name = "inn", nullable = false, length = 32)
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

        return supplierId != null ? supplierId.equals(supplier.supplierId) : supplier.supplierId == null;

    }

    @Override
    public int hashCode() {
        return supplierId != null ? supplierId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "supplierId=" + supplierId +
                ", inn='" + inn + '\'' +
                '}';
    }
}
