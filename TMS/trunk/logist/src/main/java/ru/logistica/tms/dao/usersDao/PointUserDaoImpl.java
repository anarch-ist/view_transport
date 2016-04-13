package ru.logistica.tms.dao.usersDao;

import java.util.Set;

public class PointUserDaoImpl implements GenericUserDao<PointUser> {

    private AbstractUserDao abstractUserDao = new AbstractUserDaoImpl();

    @Override
    public Set<PointUser> getAllUsers() {
        // SELECT users.userID, users.pointID, ... FROM points_users INNER JOIN (users) ON (users.userID = points_users.userID)
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public PointUser getUserById(Integer id) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Integer saveOrUpdateUser(PointUser abstractUser) {
        abstractUserDao.saveOrUpdateUser(abstractUser);
        // "INSERT INTO points_users SET...";
        throw new UnsupportedOperationException("not implemented yet");

    }
}
