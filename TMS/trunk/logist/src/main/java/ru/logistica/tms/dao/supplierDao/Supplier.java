package ru.logistica.tms.dao.supplierDao;

import ru.logistica.tms.dao.userDao.SupplierUser;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "suppliers", schema = "public")
public class Supplier {
    private Integer supplierId;
    private String inn;
    private Integer maxCells;
    private Set<SupplierUser> supplierUsers;

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

    @Basic
    @Column(name="maxcells", nullable=false)
    public int getMaxCells() {
        return this.maxCells;
    }

    public void setMaxCells(int maxCells) {
        this.maxCells = maxCells;
    }

    @OneToMany(mappedBy = "supplier")
    public Set<SupplierUser> getSupplierUsers() {
        return supplierUsers;
    }

    public void setSupplierUsers(Set<SupplierUser> supplierUsers) {
        this.supplierUsers = supplierUsers;
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
