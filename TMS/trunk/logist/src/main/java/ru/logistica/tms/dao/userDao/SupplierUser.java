package ru.logistica.tms.dao.userDao;

import ru.logistica.tms.dao.docPeriodDao.DonutDocPeriod;
import ru.logistica.tms.dao.supplierDao.Supplier;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "supplier_users", schema = "public")
@PrimaryKeyJoinColumn(name="userid")
public class SupplierUser extends User {
    private Set<DonutDocPeriod> donutDocPeriods;
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "supplierid", nullable = false)
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @OneToMany(mappedBy = "supplierUser")
    public Set<DonutDocPeriod> getDonutDocPeriods() {
        return donutDocPeriods;
    }

    public void setDonutDocPeriods(Set<DonutDocPeriod> donutDocPeriods) {
        this.donutDocPeriods = donutDocPeriods;
    }

    @Override
    public String toString() {
        return "SupplierUser{" +
                super.toString() +
                "supplier=" + supplier +
                '}';
    }
}
