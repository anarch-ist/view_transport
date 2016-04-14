package ru.logistica.tms.dao.usersDao;

import java.sql.SQLException;
import java.util.Set;

public class SupplierUserDaoImpl implements GenericUserDao<SupplierUser> {

//     private AbstractUserDaoImpl abstactUserDao; ??

    @Override
    public Set<SupplierUser> getAllUsers() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public SupplierUser getUserById(Integer id) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Integer saveOrUpdateUser(SupplierUser supplierUser) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public SupplierUser getByLogin(String login) throws SQLException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void deleteUserByLogin(String login) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
