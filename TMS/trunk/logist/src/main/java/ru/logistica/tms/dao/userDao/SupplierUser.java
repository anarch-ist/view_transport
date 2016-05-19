package ru.logistica.tms.dao.userDao;

import ru.logistica.tms.dao.supplierDao.Supplier;

import javax.persistence.*;

@Entity
@Table(name = "supplier_users", schema = "public", catalog = "postgres")
@PrimaryKeyJoinColumn(name="userid")
public class SupplierUser extends User {

    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "supplierid", nullable = false)
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return "SupplierUser{" +
                super.toString() +
                "supplier=" + supplier +
                '}';
    }
}
