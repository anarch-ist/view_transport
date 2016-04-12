package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.Supplier;

public class SupplierUser extends PrivelegedUser {
    private Supplier supplier;

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
