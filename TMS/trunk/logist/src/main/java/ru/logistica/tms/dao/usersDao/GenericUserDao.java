package ru.logistica.tms.dao.usersDao;


import java.sql.SQLException;
import java.util.Set;

public interface GenericUserDao<T extends AbstractUser> {
    Set<T> getAllUsers() throws SQLException;
    T getUserById(Integer id) throws SQLException;
    Integer saveOrUpdateUser(T user) throws SQLException;
    T getByLogin(String login) throws SQLException;
    void deleteUserByLogin(String login) throws SQLException;
}
