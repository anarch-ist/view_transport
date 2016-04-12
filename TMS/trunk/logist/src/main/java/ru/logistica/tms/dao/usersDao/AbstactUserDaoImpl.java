package ru.logistica.tms.dao.usersDao;

import java.util.Set;

public class AbstactUserDaoImpl implements GenericUserDao<AbstractUser> {
    @Override
    public Set<AbstractUser> getAllUsers() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public AbstractUser getUserById(Integer id) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Integer saveUser(AbstractUser user) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
