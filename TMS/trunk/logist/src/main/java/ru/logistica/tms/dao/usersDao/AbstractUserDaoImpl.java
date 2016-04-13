package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.constantsDao.UserRole;

import java.util.Set;

public class AbstractUserDaoImpl implements GenericUserDao<AbstractUser> {
    @Override
    public Set<AbstractUser> getAllUsers() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public AbstractUser getUserById(Integer id) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Integer saveOrUpdateUser(AbstractUser user) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public AbstractUser getByLogin(String string) {
        return null;
    }
}
