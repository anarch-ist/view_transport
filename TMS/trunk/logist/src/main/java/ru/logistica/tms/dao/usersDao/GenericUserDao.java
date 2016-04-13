package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.constantsDao.UserRole;

import java.sql.SQLException;
import java.util.Set;

public interface GenericUserDao<T extends AbstractUser> {
    Set<T> getAllUsers() throws SQLException;
    T getUserById(Integer id);
    Integer saveOrUpdateUser(T user);
    T getByLogin(String string);
    void deleteUserByLogin(String login);
}
