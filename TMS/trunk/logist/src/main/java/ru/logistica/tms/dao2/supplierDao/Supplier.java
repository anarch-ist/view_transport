package ru.logistica.tms.dao2.supplierDao;

import ru.logistica.tms.dao2.docPeriodDao.DonutDocPeriod;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "suppliers", schema = "public", catalog = "postgres")
public class Supplier {
    private Integer supplierId;
    private String inn;
    private Set<DonutDocPeriod> donutDocPeriods;

    @Id
    @Column(name = "supplierid", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "supplier")
    public Set<DonutDocPeriod> getDonutDocPeriods() {
        return donutDocPeriods;
    }

    public void setDonutDocPeriods(Set<DonutDocPeriod> donutDocPeriods) {
        this.donutDocPeriods = donutDocPeriods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Supplier supplier = (Supplier) o;

        if (supplierId != null ? !supplierId.equals(supplier.supplierId) : supplier.supplierId != null) return false;
        if (inn != null ? !inn.equals(supplier.inn) : supplier.inn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = supplierId != null ? supplierId.hashCode() : 0;
        result = 31 * result + (inn != null ? inn.hashCode() : 0);
        return result;
    }

}
