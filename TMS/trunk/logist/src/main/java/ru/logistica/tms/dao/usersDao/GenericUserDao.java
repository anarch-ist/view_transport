package ru.logistica.tms.dao.usersDao;

import java.util.Set;

public interface GenericUserDao<T> {
    Set<T> getAllUsers();
    T getUserById(Integer id);
    Integer saveUser(T user);
}
